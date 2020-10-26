package jpdemo.reactivegreeting.restgreeting.controller;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class GreetingRestControllerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("Greeting defaults to request name of World")
    void greetingRequestResponse_NoInputDefaultNameIsWorld() {

        // when
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/greeting/request").build())
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(equalTo("Hello World"))
        ;

    }

    @Test
    @DisplayName("Greeting returned uses name")
    void greetingRequestResponse_WithInput() {
        String name = "Bob";


        // when
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/greeting/request").queryParam("name", name).build())
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(equalTo("Hello Bob"));
    }

    @Test
    @DisplayName("Log returns nothing")
    void greetingLog() {
        // given
        String name = "LogName";
        GreetingRequest request = requestBuilder("LogName");

        // when
        // when
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/greeting/log").queryParam("name", name).build())
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody()
                .isEmpty()
        ;

    }


    private GreetingRequest requestBuilder(String name) {
        return GreetingRequest.newBuilder().setName(name).build();
    }

    private GreetingResponse responseBuilder(String greeting) {
        return GreetingResponse.newBuilder().setGreeting(greeting).build();
    }


}