KEYCLOAK_PATH ?= ../keycloak-15.0.2

install:
	mvn clean install

cp-jars: remove-jars
	cp ./target/keycloak-to-gcpubsub-1.0.1.jar $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar

remove-jars:
	rm $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar || true
	rm $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar.deployed || true
	rm $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar.undeployed || true