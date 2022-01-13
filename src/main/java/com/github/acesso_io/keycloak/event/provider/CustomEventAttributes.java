package com.github.acesso_io.keycloak.event.provider;
import org.keycloak.events.Event;

public class CustomEventAttributes extends Event  {

	private String appName;

	public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}