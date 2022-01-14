package com.github.acesso_io.keycloak.event.provider;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.keycloak.events.admin.AdminEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
@JsonTypeInfo(use = Id.CLASS)
public class EventAdminNotificationGcpsMsg extends CustomAdminEventAttributes implements Serializable  {

	private static final long serialVersionUID = -7367949289101799624L;

	public static EventAdminNotificationGcpsMsg create(AdminEvent adminEvent, String appName) {
		EventAdminNotificationGcpsMsg msg = new EventAdminNotificationGcpsMsg();
		msg.setAuthDetails(adminEvent.getAuthDetails());
		msg.setError(adminEvent.getError());
		msg.setOperationType(adminEvent.getOperationType());
		msg.setRealmId(adminEvent.getRealmId());
		msg.setRepresentation(adminEvent.getRepresentation());
		msg.setResourcePath(adminEvent.getResourcePath());
		msg.setResourceType(adminEvent.getResourceType());
		msg.setResourceTypeAsString(adminEvent.getResourceTypeAsString());
		msg.setTime(adminEvent.getTime());
		msg.setAppName(appName);

		return msg;
	}
}
