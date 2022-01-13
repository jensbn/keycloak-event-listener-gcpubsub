package com.github.acesso_io.keycloak.event.provider;

public class Helper {

	static String resolveConfigVar(String envVariableName) {
		
		String value = "KEYCLOAK";
		if(System.getenv(envVariableName) != null) {
			value = System.getenv(envVariableName);
		}
		return value;
	}
}
