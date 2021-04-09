/*
 * Copyright 2021 clocken
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
