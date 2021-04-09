package com.github.clocken.spring.webclient.oauth2.login.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

import java.time.Duration;
import java.time.Instant;

/**
 * {@link TestComponent} for a {@link OAuth2AuthorizedClientManager}-Mock, which always returns an initialized {@link OAuth2AuthorizedClient} to skip the
 * <a href=https://tools.ietf.org/html/rfc6749#section-1.3.4>OAuth 2.0 - Client Credentials Flow</a>
 * with a real server when integration testing in a servlet environment.
 * <br>
 * <br>
 * <strong>NOTE:</strong> Import this in your test via:
 * <pre>@Import(AlwaysAuthorizedServletOAuth2AuthorizedClientManager.class)</pre>
 */
@TestComponent
@Primary
public class AlwaysAuthorizedServletOAuth2AuthorizedClientManager implements OAuth2AuthorizedClientManager {

    @Value("${spring.security.oauth2.client.registration.api-client.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.api-client.client-secret}")
    String clientSecret;

    @Value("${spring.security.oauth2.client.provider.some-keycloak.token-uri}")
    String tokenUri;

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuth2AuthorizedClient authorize(final OAuth2AuthorizeRequest authorizeRequest) {
        return new OAuth2AuthorizedClient(
                ClientRegistration
                        .withRegistrationId("api-client")
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .tokenUri(tokenUri)
                        .build(),
                "some-keycloak",
                new OAuth2AccessToken(TokenType.BEARER,
                        "c29tZS10b2tlbg==",
                        Instant.now().minus(Duration.ofMinutes(1)),
                        Instant.now().plus(Duration.ofMinutes(4))));
    }
}
