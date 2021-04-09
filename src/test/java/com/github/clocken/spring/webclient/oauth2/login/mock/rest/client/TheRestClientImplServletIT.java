package com.github.clocken.spring.webclient.oauth2.login.mock.rest.client;

import com.github.clocken.spring.webclient.oauth2.login.mock.AbstractIntegrationTestWithMockServer;
import com.github.clocken.spring.webclient.oauth2.login.mock.AlwaysAuthorizedServletOAuth2AuthorizedClientManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static com.github.clocken.spring.webclient.oauth2.login.mock.rest.MockServerUtils.someResourceRequestWithMockedOAuth2Token;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// comment this out to see, that it really bypasses the Client Credentials flow
@Import(AlwaysAuthorizedServletOAuth2AuthorizedClientManager.class)
class TheRestClientImplServletIT extends AbstractIntegrationTestWithMockServer {

    @Value("${mockserver.api.url}")
    URI mockServerApiUrl;

    @Autowired
    WebClient.Builder webClientBuilder;

    private TheRestClient restClient;

    private MockServerClient mockServer;

    @BeforeEach
    void setUpTest() {
        restClient = new TheRestClientImpl(
                webClientBuilder,
                mockServerApiUrl.toString());
    }

    @Test
    void should_get_some_resource_with_mocked_oauth2_token() {
        final Mono<ResponseEntity<String>> request = restClient.getSomething();

        StepVerifier
                .create(request.log())
                .assertNext(response -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK))
                .verifyComplete();

        mockServer.verify(
                someResourceRequestWithMockedOAuth2Token(),
                VerificationTimes.once());
    }
}
