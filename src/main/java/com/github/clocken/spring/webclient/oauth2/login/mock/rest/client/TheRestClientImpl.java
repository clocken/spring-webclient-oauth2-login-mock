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

package com.github.clocken.spring.webclient.oauth2.login.mock.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TheRestClientImpl implements TheRestClient {

    private final WebClient webClient;

    @Autowired
    public TheRestClientImpl(final WebClient.Builder webClientBuilder, @Value("${some-api.base-url}") final String apiBaseUrl) {
        webClient = webClientBuilder
                .baseUrl(apiBaseUrl)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<String>> getSomething() {
        return webClient
                .get()
                .uri("/some-resource")
                .retrieve()
                .toEntity(String.class);
    }
}
