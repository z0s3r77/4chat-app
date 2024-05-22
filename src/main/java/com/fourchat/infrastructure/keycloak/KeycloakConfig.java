package com.fourchat.infrastructure.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class KeycloakConfig {

    @Value("${4chat.keycloak.url}")
    private String serverUrl;

    @PostConstruct
    public void init() {
        KeycloakProvider.setServerUrl(serverUrl);
    }
}
