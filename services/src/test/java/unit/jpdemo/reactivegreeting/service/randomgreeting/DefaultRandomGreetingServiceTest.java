package jpdemo.reactivegreeting.service.randomgreeting;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingSetup;
import jpdemo.proto.greeting.v1.RandomGreetingRequest;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.test.utils.LoggerTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRandomGreetingServiceTest {

    private DefaultRandomGreetingService classUnderTest;
    private ListAppender<ILoggingEvent> loggingEventListAppender;
    private final List<String> POSSIBLE_NAMES  =Arrays.asList("Odin", "Thor", "Frigg", "Balder", "Loki", "Freya");
    private final List<String> POSSIBLE_GREETINGS  =  Arrays.asList("Hi", "Hello", "Hola", "Sawabona", "Hey");

    @BeforeEach
    void setUp() {
        loggingEventListAppender = LoggerTestUtil.getListAppenderForClass(DefaultGreetingService.class);
        classUnderTest = new DefaultRandomGreetingService();
    }


    @AfterEach
    void tearDown() {
        loggingEventListAppender.stop();
        loggingEventListAppender = null;
        classUnderTest = null;
    }

    @Test
    public void randomGreeting(){

        var request = RandomGreetingRequest.newBuilder().build();

        // when
        var response = classUnderTest.randomGreeting(request,null).block();

        // then
        assertNotNull(response);
        assertTrue(greetingsContains(response.getGreeting(),POSSIBLE_GREETINGS));
        assertTrue(greetingsContains(response.getGreeting(),POSSIBLE_NAMES));

    }

    @Test
    public void randomGreetings(){

        // given
        var request = RandomGreetingRequest.newBuilder().build();
        var ITEMS_TO_TAKE= 4;

        // when
        var responseStream = classUnderTest.randomGreetings(request,null).take(ITEMS_TO_TAKE);

        //then

        StepVerifier.create(responseStream)
                .assertNext(g->{
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_GREETINGS));
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_NAMES));
                })
                .assertNext(g->{
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_GREETINGS));
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_NAMES));
                })
                .assertNext(g->{
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_GREETINGS));
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_NAMES));
                })
                .assertNext(g->{
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_GREETINGS));
                    assertTrue(greetingsContains(g.getGreeting(),POSSIBLE_NAMES));
                })
                .verifyComplete();
    }


    private boolean greetingsContains(String greeting, List<String> values){
        for (String value:values) {
            if(greeting.contains(value)){
                return true;
            }
        }
        return false;
    }
}