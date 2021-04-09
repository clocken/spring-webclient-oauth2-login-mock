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
