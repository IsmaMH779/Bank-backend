package com.example.auth_service.adapters.out.mapper;

import com.example.auth_service.domain.model.ClientApp;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Date;

public class ClientAppMapper {

    public static RegisteredClient toRegisteredClient(ClientApp clientApp) {

        RegisteredClient client = RegisteredClient.withId(clientApp.getClientId())
                .clientId(clientApp.getClientId())
                .clientSecret(clientApp.getClientSecret())
                .clientIdIssuedAt(new Date(System.currentTimeMillis()).toInstant())
                .clientAuthenticationMethods(clientAuthMethods -> {
                    clientApp.getClientAuthenticationMethods().stream()
                            .map(method -> new ClientAuthenticationMethod(method))
                            .forEach(clientAuthMethods::add);
                })
                .authorizationGrantTypes(authGrantTypes -> {
                    clientApp.getAuthorizationGrantTypes().stream()
                            .map(grantType -> new AuthorizationGrantType(grantType))
                            .forEach(authGrantTypes::add);
                })
                .redirectUris(redirectUris -> {
                    clientApp.getRedirectUris().stream().
                            forEach(redirectUris::add);
                })
                .scopes(scopes -> {
                    clientApp.getScopes().stream().forEach(scopes::add);
                })
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(clientApp.getDurationInMinutes()))
                        .refreshTokenTimeToLive(Duration.ofMinutes(clientApp.getDurationInMinutes() * 4))
                        .build()
                )
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(clientApp.isRequiredProofKey())
                        .build()
                )
                .build();

        return client;
    };

}