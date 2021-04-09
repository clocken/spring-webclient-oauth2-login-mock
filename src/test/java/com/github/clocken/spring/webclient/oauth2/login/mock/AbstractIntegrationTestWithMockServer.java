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
