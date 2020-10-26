package jpdemo.reactivegreeting.restgreeting.controller;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = GreetingRestController.class)
@ActiveProfiles("test")
class GreetingRestControllerWebClientTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private DefaultGreetingService greetingService;
    @MockBean
    private DefaultRandomGreetingService randomGreetingService;

    @Test
    @DisplayName("Greeting defaults to request name of World")
    void greetingRequestResponse_NoInputDefaultNameIsWorld() {
        // given
        String name = "World";
        var mockResponse = Mono.just(responseBuilder("Hello World"));
        given(greetingService.greeting(requestBuilder(name), null)).willReturn(mockResponse);

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
        var mockResponse = Mono.just(responseBuilder("Hello Bob"));
        given(greetingService.greeting(requestBuilder(name), null)).willReturn(mockResponse);

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

        //then
        Mockito.verify(greetingService).logGreeting(request, null);
    }

    @Test
    @DisplayName("Greeting channel")
    void channel(){

    }

    @Test
    @DisplayName("Greeting returned uses name")
    void randomGreeting(){
        // given
        String greeting = "Hey Thor";
        var mockResponse = Mono.just(responseBuilder(greeting));
        given(randomGreetingService.randomGreeting(null, null)).willReturn(mockResponse);

        // when
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/greeting/random").build())
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(equalTo(greeting))
        ;
    }

    @Test
    @DisplayName("Greetings stream")
    void randomGreetingStream(){
        // given
        String mockGreeting1 = "Hey Thor";
        String mockGreeting2 = "Hola Odin";
        var mockGreetings = Arrays.asList(responseBuilder(mockGreeting1),responseBuilder(mockGreeting2));
        var mockResponse = Flux.fromIterable(mockGreetings);
        given(randomGreetingService.randomGreetings(null, null)).willReturn(mockResponse);

        // when
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/greeting/stream").build())
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBodyList(String.class)
                .value(List::size,equalTo(2))
                .value(response1 -> response1.get(0),equalTo(mockGreeting1))
                .value(response2 -> response2.get(1),equalTo(mockGreeting2))
        ;
    }


    private GreetingRequest requestBuilder(String name) {
        return GreetingRequest.newBuilder().setName(name).build();
    }

    private GreetingResponse responseBuilder(String greeting) {
        return GreetingResponse.newBuilder().setGreeting(greeting).build();
    }


}