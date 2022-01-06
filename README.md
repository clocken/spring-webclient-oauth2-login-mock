# spring-webclient-oauth2-login-mock

This is a demo project to show how to bypass
the [OAuth 2.0 Client Credentials Flow](https://tools.ietf.org/html/rfc6749#section-1.3.4) in a Spring Boot Integration
Test. This is useful for projects using
the [Spring WebClient](https://docs.spring.io/spring-framework/docs/5.3.5/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html)
to interact with OAuth-2.0-secured resources in a machine-to-machine scenario with no user interaction.

## Usage

Clone this repository and checkout either

* branch [reactive-stack](https://github.com/clocken/spring-webclient-oauth2-login-mock/tree/reactive-stack) for a
  completely Reactive Spring WebFlux based demo or
* branch [servlet-stack](https://github.com/clocken/spring-webclient-oauth2-login-mock/tree/servlet-stack) for a
  Servlet-Stack based or mixed-Stack based (meaning Servlet-Stack and Reactive WebFlux) demo

Then run `mvn test -Dtest=TheRestClientImplIT` to see an integration test of
the [TheRestClientImpl](src/main/java/com/github/clocken/spring/webclient/oauth2/login/mock/rest/client/TheRestClientImpl.java)
using
the [Spring WebClient](https://docs.spring.io/spring-framework/docs/5.3.5/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html)
to fetch a secured resource using a stubbed Authorization Token:

<pre><code>
2021-04-09 21:24:59.886  INFO 28221 --- [erver-EventLog0] org.mockserver.log.MockServerEventLog    : 62997 received request:

  {
    "method" : "GET",
    "path" : "/api/v1/some-resource",
    "headers" : {
      "accept-encoding" : [ "gzip" ],
      "user-agent" : [ "ReactorNetty/1.0.5" ],
      "host" : [ "127.0.0.1:62997" ],
      "accept" : [ "*/*" ],
      <b>"Authorization" : [ "Bearer c29tZS10b2tlbg==" ]</b>,
      "content-length" : [ "0" ]
    },
    "keepAlive" : true,
    "secure" : false
  }
</code></pre>

## How it works

The project is a Spring Boot project configured for OAuth 2.0 Client functionality with Spring Security -
see [WebClientOAuth2Configuration](src/main/java/com/github/clocken/spring/webclient/oauth2/login/mock/configuration/WebClientOAuth2Configuration.java)
and [application.yml](src/main/resources/application.yml).

This configures
any [Spring WebClient](https://docs.spring.io/spring-framework/docs/5.3.5/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html)
in the application provided by the
autoconfigured [WebClient.Builder](https://docs.spring.io/spring-framework/docs/5.3.5/javadoc-api/org/springframework/web/reactive/function/client/WebClient.Builder.html)
to transparently perform an [OAuth 2.0 Client Credentials Flow](https://tools.ietf.org/html/rfc6749#section-1.3.4) on
every REST-Resource request, using
an [OAuth2AuthorizedClientManager](https://docs.spring.io/spring-security/site/docs/5.4.5/api/org/springframework/security/oauth2/client/OAuth2AuthorizedClientManager.html)
or
a [ReactiveOAuth2AuthorizedClientManager](https://docs.spring.io/spring-security/site/docs/5.4.5/api/org/springframework/security/oauth2/client/ReactiveOAuth2AuthorizedClientManager.html)
(depending on the selected Web Stack).

So, to bypass this Flow in an integration test we can provide a mock implementation `@Bean` of the
configured `xxOAuth2AuthorizedClientManager` in the `TestContext`. This mock implementation is located
in [AlwaysAuthorizedOAuth2AuthorizedClientManager](src/test/java/com/github/clocken/spring/webclient/oauth2/login/mock/AlwaysAuthorizedOAuth2AuthorizedClientManager.java)
(notice the `@Primary` and `@TestComponent` annotations) and is `@Import`ed in the integration test
class [TheRestClientImplIT](src/test/java/com/github/clocken/spring/webclient/oauth2/login/mock/rest/client/TheRestClientImplIT.java)
. It simply returns a new instance of
an [OAuth2AuthorizedClient](https://docs.spring.io/spring-security/site/docs/5.4.5/api/org/springframework/security/oauth2/client/OAuth2AuthorizedClient.html)
(or a [Mono](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html) of that on Reactive
Stack)
stubbed with a valid OAuth 2.0 Client Credentials Authorization and Token, thus no Authorization request is ever needed
for any REST-Resource request.

The other classes in this project under `src/test/java` are just helper classes for configuring
the [MockServer](https://www.mock-server.com/) for the REST-Resource request (the one the integration test is normally
interested in). Also, we can monitor its logs to see that the stubbed Authorization Token is really used for the
request. While I find this one convenient, you may of course use any mock server you like in your project ;)

## Further reading

* [Spring Boot WebClient Customization](https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/spring-boot-features.html#boot-features-webclient-customization)
* [Spring Boot OAuth 2.0 Autoconfiguration](https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/spring-boot-features.html#boot-features-security-oauth2-client)
* [Spring Security OAuth 2.0 WebClient configuration (Servlet Stack)](https://docs.spring.io/spring-security/site/docs/5.4.5/reference/html5/#oauth2Client-webclient-servlet)
* [Spring Security OAuth 2.0 WebClient configuration (Reactive Stack)](https://docs.spring.io/spring-security/site/docs/5.4.5/reference/html5/#webclient-setup)
* [Spring Security Testing OAuth 2.0 Clients](https://docs.spring.io/spring-security/site/docs/5.4.5/reference/html5/#webflux-testing-oauth2-client)
  (This gave me the crucial hint for my solution to just stub an `OAuth2AuthorizedClient` from the
  mock `xxOAuth2AuthorizedClientManager`)
* [Baeldung - WebClient Client Credentials Flow](https://www.baeldung.com/spring-webclient-oauth2#spring-security-5-support---the-client-credentials-flow)
  (Beware that this write-up uses the now deprecated `UnAuthenticatedServerOAuth2AuthorizedClientRepository`, that is
  why I opted for `ReactiveOAuth2AuthorizedClientManager` in my solution)
