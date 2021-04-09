package com.github.clocken.spring.webclient.oauth2.login.mock;

import com.github.clocken.spring.webclient.oauth2.login.mock.rest.MockServerConstants;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.mockserver.springtest.MockServerTest;

import java.io.IOException;
import java.util.UUID;

import static com.github.clocken.spring.webclient.oauth2.login.mock.rest.MockServerUtils.okResponse;
import static com.github.clocken.spring.webclient.oauth2.login.mock.rest.MockServerUtils.someResourceRequestWithMockedOAuth2Token;

@MockServerTest("mockserver.api.url=http://127.0.0.1:${mockServerPort}" + MockServerConstants.API_URI_PATH)
public abstract class AbstractIntegrationTestWithMockServer {

    private MockServerClient mockServer;

    @BeforeEach
    void setUp() throws IOException {
        mockServer
                .when(
                        someResourceRequestWithMockedOAuth2Token())
                .respond(
                        okResponse(() -> UUID.randomUUID().toString()));
    }
}
