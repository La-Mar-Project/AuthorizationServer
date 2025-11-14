package com.jjubul.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Instant;
import java.util.UUID;

@Configuration
public class ClientConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("jjubul-web-client")
                .clientName("jjubul-web-client")
                .clientIdIssuedAt(Instant.now())
                .clientSecretExpiresAt(Instant.MAX)
                .clientSecret("1234")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .redirectUri("http://54.180.162.154:9000/login/oauth2/code/jjubul-web-client")
                .redirectUri("http://54.180.162.154:9001/login/oauth2/code/jjubul-web-client")
                .redirectUri("http://54.180.162.154:9001/login/oauth2/code/jjubul-web-client")
                .redirectUri("http://54.180.162.154:9000")
                .redirectUri("http://54.180.162.154:9001")
                .redirectUri("http://54.180.162.154:9001")
                .scope("openid")
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }
}
