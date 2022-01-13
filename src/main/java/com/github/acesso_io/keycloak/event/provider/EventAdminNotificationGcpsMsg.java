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
public class EventAdminNotificationGcpsMsg extends AdminEvent implements Serializable  {

	private static final long serialVersionUID = -7367949289101799624L;
	private static final String ENV_VAR_APP_NAME = "KC_TO_GCP_APP_NAME";

	private String appName;

	public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

	public static EventAdminNotificationGcpsMsg create(AdminEvent adminEvent) {
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
		msg.setAppName(Helper.resolveConfigVar(ENV_VAR_APP_NAME));

		return msg;
	}
}
