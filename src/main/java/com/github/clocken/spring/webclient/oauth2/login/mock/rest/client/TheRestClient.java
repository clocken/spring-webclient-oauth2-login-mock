package com.github.clocken.spring.webclient.oauth2.login.mock.rest.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface TheRestClient {

    Mono<ResponseEntity<String>> getSomething();
}
