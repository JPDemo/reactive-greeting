package jpdemo.reactivegreeting.restgreeting.controller;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GreetingRestControllerTest {

    @MockBean
    private DefaultGreetingService greetingService;
    @MockBean
    private DefaultRandomGreetingService randomGreetingService;

    private final static String TEST_NAME = "Jason";

    private GreetingRestController classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new GreetingRestController(greetingService,randomGreetingService);
    }

    @AfterEach
    void tearDown() {
        classUnderTest = null;
    }

    @Test
    void greetingRequestResponse() {
        // given
        var request = requestBuilder(TEST_NAME);
        GreetingResponse mockResponse = responseBuilder("Hello Jason");
        when(greetingService.greeting(request, null)).thenReturn(Mono.just(mockResponse));

        // when
        classUnderTest.greetingRequestResponse(TEST_NAME);

        //then
        Mockito.verify(greetingService).greeting(request, null);
    }

    @Test
    void greetingChannel() {
        // given
        var request = Flux.just(requestBuilder("Bob"));
        GreetingResponse mockResponse = responseBuilder("Hello Bob");
        when(greetingService.greetings(request, null)).thenReturn(Flux.just(mockResponse));

        // when
        classUnderTest.greetingChannel(request);

        //then
        Mockito.verify(greetingService).greetings(request, null);
    }

    @Test
    void greetingLog() {
        // given
        GreetingRequest request = requestBuilder("Jason");

        // when
        classUnderTest.greetingLog(TEST_NAME);

        //then
        Mockito.verify(greetingService).logGreeting(request, null);
    }

    @Test
    void randomGreetings() {
        // given
        GreetingResponse mockResponse = responseBuilder("Hello Jason");
        when(randomGreetingService.randomGreetings(null, null)).thenReturn(Flux.just(mockResponse));

        // when
        classUnderTest.randomGreetings();

        //then
        Mockito.verify(randomGreetingService).randomGreetings(null, null);
    }

    @Test
    void randomGreeting() {
        // given
        GreetingResponse mockResponse = responseBuilder("Hello Jason");
        when(randomGreetingService.randomGreeting(null, null)).thenReturn(Mono.just(mockResponse));

        // when
        classUnderTest.randomGreeting();

        //then
        Mockito.verify(randomGreetingService).randomGreeting(null, null);
    }
    
    private GreetingRequest requestBuilder(String name) {
        return GreetingRequest.newBuilder().setName(name).build();
    }

    private GreetingResponse responseBuilder(String greeting) {
        return GreetingResponse.newBuilder().setGreeting(greeting).build();
    }
}