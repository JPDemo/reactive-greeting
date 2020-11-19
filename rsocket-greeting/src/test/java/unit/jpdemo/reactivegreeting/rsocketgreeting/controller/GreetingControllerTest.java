package jpdemo.reactivegreeting.rsocketgreeting.controller;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingSetup;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class GreetingControllerTest {

    @MockBean
    private DefaultGreetingService greetingService;
    @MockBean
    private DefaultRandomGreetingService randomGreetingService;

    @Autowired
    ApplicationContext context;


    private GreetingController classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new GreetingController(greetingService, randomGreetingService);

    }

    @AfterEach
    void tearDown() {
        classUnderTest = null;
    }

    @Test
    @DisplayName("setup: Test error thrown when config is null")
    void setup_whenConfigIsNull() {

        Throwable exceptionThatWasThrown = assertThrows(IllegalArgumentException.class, () -> classUnderTest.setup(null));
        assertEquals("Config should not be null", exceptionThatWasThrown.getMessage());
    }

    @Test
    @DisplayName("setup: assert setup on service is called")
    void setup_whenConfigIsNotNull() {
        // given
        GreetingSetup setupConfig = GreetingSetup.newBuilder().setLocale(GreetingSetup.LocaleType.LOCALE_EN).build();

        // when
        classUnderTest.setup(setupConfig);

        // then
        Mockito.verify(greetingService).setup(setupConfig);
    }

    @Test
    void greetingRequestResponse() {
        // given
        GreetingRequest request = requestBuilder("Jason");
        GreetingResponse mockResponse = responseBuilder("Hello Jason");
        when(greetingService.greeting(request, null)).thenReturn(Mono.just(mockResponse));

        // when
        var response = classUnderTest.greetingRequestResponse(null,request).block();

        //then
        Mockito.verify(greetingService).greeting(request, null);
        assertEquals(mockResponse, response);
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
        classUnderTest.greetingLog(request);

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