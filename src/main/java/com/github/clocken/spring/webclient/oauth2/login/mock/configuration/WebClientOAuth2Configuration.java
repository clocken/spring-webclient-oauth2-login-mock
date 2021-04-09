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
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;

/**
 * Use this only in a Reactive environment
 */
@Configuration
public class WebClientOAuth2Configuration {

    @Bean
    public WebClientCustomizer webClientOAuthCustomizer(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        return webClientBuilder -> {
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2ClientCredentialsFilter =
                    new ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveOAuth2AuthorizedClientManager);
            oauth2ClientCredentialsFilter.setDefaultClientRegistrationId("api-client");

            webClientBuilder
                    .filter(oauth2ClientCredentialsFilter);
        };
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(ReactiveClientRegistrationRepository clientRegistrations,
                                                                                       ReactiveOAuth2AuthorizedClientService authorizedClients) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, authorizedClients);

        authorizedClientManager.setAuthorizedClientProvider(
                new ClientCredentialsReactiveOAuth2AuthorizedClientProvider());

        return authorizedClientManager;
    }
}
