package com.github.acesso_io.keycloak.event.provider;
import org.keycloak.events.admin.AdminEvent;

public class CustomAdminEventAttributes extends AdminEvent  {

	private String appName;

	public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}