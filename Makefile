LOCAL_KEYCLOAK_PATH ?= ../keycloak-15.0.2
KEYCLOAK_PATH ?= ../../docker-build

install:
	mvn clean install

cp-local-jars: remove-local-jars
	cp ./target/keycloak-to-gcpubsub-1.0.1.jar $(LOCAL_KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar

remove-local-jars:
	rm $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar || true
	rm $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar.deployed || true
	rm $(KEYCLOAK_PATH)/standalone/deployments/keycloak-to-gcpubsub-1.0.1.jar.undeployed || true

cp-jars: remove-jars
	mkdir -p $(KEYCLOAK_PATH)
	cp ./target/keycloak-to-gcpubsub*.jar $(KEYCLOAK_PATH)/

remove-jars:
	rm $(KEYCLOAK_PATH)/keycloak-to-gcpubsub*.jar || true