package com.github.acesso_io.keycloak.event.provider;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.jboss.logging.Logger;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Publisher.Builder;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GcPubSubEventListenerProvider implements EventListenerProvider {

	private static Logger logger = Logger.getLogger(GcPubSubEventListenerProvider.class);
	private GcPubSubConfig cfg;
	// private ConnectionFactory factory;

	public GcPubSubEventListenerProvider(GcPubSubConfig cfg) {
		this.cfg = cfg;
	}

	@Override
	public void close() {
	}

	@Override
	public void onEvent(Event event) {
		CustomEventAttributes customAttributes = getCustomEventAttributes();
		EventClientNotificationGcpsMsg msg = EventClientNotificationGcpsMsg.create(event, customAttributes.getAppName());
		Map<String, String> messageAttributes = GcPubSubAttributes.createMap(event);
		String messageString = GcPubSubConfig.writeAsJson(msg, true);
		String topicId = cfg.getAdminEventTopicId();

		this.publishNotification(topicId, messageString, messageAttributes);
	}

	private CustomEventAttributes getCustomEventAttributes() {
		CustomEventAttributes customAttributes = new CustomEventAttributes();
		customAttributes.setAppName(cfg.getAppNameId());
		return customAttributes;
	}

	@Override
	public void onEvent(AdminEvent event, boolean includeRepresentation) {
		CustomAdminEventAttributes customAttributes = getCustomAdminEventAttributes();
		EventAdminNotificationGcpsMsg msg = EventAdminNotificationGcpsMsg.create(event,  customAttributes.getAppName());
		Map<String, String> messageAttributes = GcPubSubAttributes.createMap(event);
		String messageString = GcPubSubConfig.writeAsJson(msg, true);
		String topicId = cfg.getAdminEventTopicId();

		this.publishNotification(topicId, messageString, messageAttributes);
	}

	private CustomAdminEventAttributes getCustomAdminEventAttributes() {
		CustomAdminEventAttributes customAttributes = new CustomAdminEventAttributes();
		customAttributes.setAppName(cfg.getAppNameId());
		return customAttributes;
	}

	private void publishNotification(String topicId, String messageString, Map<String, String> attributes) {

		TopicName topicName = TopicName.of(cfg.getProjectId(), topicId);

		Publisher publisher = null;

		// See: https://cloud.google.com/pubsub/docs/emulator#pubsub-emulator-java
		String emulatorHostport = System.getenv("PUBSUB_EMULATOR_HOST");
		ManagedChannel channel = null;

		if (emulatorHostport != null) {
			channel = ManagedChannelBuilder.forTarget(emulatorHostport).usePlaintext().build();
		}
		try {
			Builder publisherBuilder = Publisher.newBuilder(topicName);

			if (emulatorHostport != null) {
				TransportChannelProvider channelProvider =
				FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
				CredentialsProvider credentialsProvider = NoCredentialsProvider.create();
				publisherBuilder.setChannelProvider(channelProvider);
				publisherBuilder.setCredentialsProvider(credentialsProvider);
			}

			publisher = publisherBuilder.build();

			String message = messageString;
			ByteString data = ByteString.copyFromUtf8(message);
			PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).putAllAttributes(attributes).build();

			// Once published, returns a server-assigned message id (unique within the topic)
			ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
			String messageId = messageIdFuture.get();
			logger.info("Published message ID: " + messageId);
		} catch (IOException | ExecutionException | InterruptedException ex) {
			logger.error("keycloak-to-gcpubsub ERROR sending message: " + attributes, ex);
		}
		finally {
			if (emulatorHostport != null) {
				channel.shutdown();
			}
			else if (publisher != null) {
				try {
					// When finished with the publisher, shutdown to free up resources.
					publisher.shutdown();
					publisher.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException ex) {
					logger.error("keycloak-to-gcpubsub ERROR shutting down publisher: " + attributes, ex);
				}
			}
		}

	}

}