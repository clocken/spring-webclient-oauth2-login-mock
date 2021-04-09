package com.github.clocken.spring.webclient.oauth2.login.mock.configuration;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ClientCredentialsOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;

/**
 * Use this only in a servlet environment.
 */
@Configuration
public class WebClientOAuth2Configuration {

    @Bean
    public WebClientCustomizer webClientOAuthCustomizer(final OAuth2AuthorizedClientManager authorizedClientManager) {
        return webClientBuilder -> {
            final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2ClientCredentialsFilter =
                    new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
            oauth2ClientCredentialsFilter.setDefaultClientRegistrationId("api-client");

            webClientBuilder
                    .filter(oauth2ClientCredentialsFilter);
        };
    }

    @Bean
    public OAuth2AuthorizedClientManager oauth2AuthorizedClientManager(final ClientRegistrationRepository clientRegistrationRepository,
                                                                       final OAuth2AuthorizedClientRepository authorizedClientRepository) {
        final DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(
                new ClientCredentialsOAuth2AuthorizedClientProvider());

        return authorizedClientManager;
    }
}
