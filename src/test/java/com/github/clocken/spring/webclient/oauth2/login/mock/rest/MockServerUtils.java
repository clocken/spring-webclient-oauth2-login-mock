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

package com.github.clocken.spring.webclient.oauth2.login.mock.rest;

import org.mockserver.mock.action.ExpectationResponseCallback;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.RequestDefinition;

import java.util.function.Supplier;

public final class MockServerUtils {

    private MockServerUtils() {
        // don't instantiate me
    }

    public static RequestDefinition someResourceRequestWithMockedOAuth2Token() {
        return HttpRequest
                .request(MockServerConstants.API_URI_PATH + "/some-resource")
                .withMethod("GET")
                .withHeader(Header
                        .header("Authorization", "Bearer c29tZS10b2tlbg=="));
    }

    public static ExpectationResponseCallback okResponse(final Supplier<String> bodySupplier) {
        return httpRequest -> {
            final HttpResponse httpResponse = HttpResponse
                    .response()
                    .withStatusCode(200);
            return bodySupplier != null ? httpResponse.withBody(bodySupplier.get()) : httpResponse;
        };
    }

    public static ExpectationResponseCallback failResponse(final int statusCode, final Supplier<String> bodySupplier) {
        return httpRequest -> {
            final HttpResponse httpResponse = HttpResponse
                    .response()
                    .withStatusCode(statusCode);
            return bodySupplier != null ? httpResponse.withBody(bodySupplier.get()) : httpResponse;
        };
    }
}
