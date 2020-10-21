package jpdemo.reactivegreeting.service.greeting;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingSetup;
import jpdemo.reactivegreeting.service.test.utils.LoggerTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class DefaultGreetingServiceTest {

    private DefaultGreetingService classUnderTest;
    private ListAppender<ILoggingEvent> loggingEventListAppender;
    private final String TEST_NAME = "Thor";

    @BeforeEach
    void setUp() {
        loggingEventListAppender = LoggerTestUtil.getListAppenderForClass(DefaultGreetingService.class);
        classUnderTest = new DefaultGreetingService();
        classUnderTest.setup(buildConfig(GreetingSetup.LocaleType.LOCALE_UNSET));
    }

    @AfterEach
    void tearDown() {
        loggingEventListAppender.stop();
        loggingEventListAppender = null;
        classUnderTest = null;
    }

    @Test
    void setup() {

        // given
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        var config = GreetingSetup.newBuilder()
                .setLocale(GreetingSetup.LocaleType.LOCALE_EN)
                .setInitiated(timestamp)
                .build();

        // when
        classUnderTest.setup(config);

        // then
        assertEquals(config,classUnderTest.getConfig() );
       /* assertThat(loggingEventListAppender.list)
                .extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
                .contains(Tuple.tuple("Config setup invoked with", Level.INFO));*/
    }

    @Test
    void greeting_LocaleEn() {

        // given
        classUnderTest.setup(buildConfig(GreetingSetup.LocaleType.LOCALE_EN));
        var request = GreetingRequest.newBuilder().setName(TEST_NAME).build();

        // when
        var response = classUnderTest.greeting(request,null).block();

        // then
        assertNotNull(response);
        assertEquals("Hi Thor",response.getGreeting());
    }

    @Test
    void greeting_LocaleDe() {

        // given
        classUnderTest.setup(buildConfig(GreetingSetup.LocaleType.LOCALE_DE));
        var request = GreetingRequest.newBuilder().setName(TEST_NAME).build();

        // when
        var response = classUnderTest.greeting(request,null).block();

        // then
        assertNotNull(response);
        assertEquals("Guten tag Thor",response.getGreeting());
    }

    @Test
    void greeting_LocaleEs() {

        // given
        classUnderTest.setup(buildConfig(GreetingSetup.LocaleType.LOCALE_ES));
        var request = GreetingRequest.newBuilder().setName(TEST_NAME).build();

        // when
        var response = classUnderTest.greeting(request,null).block();

        // then
        assertNotNull(response);
        assertEquals("Hola Thor",response.getGreeting());
    }

    @Test
    void greeting_LocaleUnset() {

        // given
        classUnderTest.setup(buildConfig(GreetingSetup.LocaleType.LOCALE_UNSET));
        var request = GreetingRequest.newBuilder().setName(TEST_NAME).build();

        // when
        var response = classUnderTest.greeting(request,null).block();

        // then
        assertNotNull(response);
        assertEquals("Hello Thor",response.getGreeting());
    }


    @Test
    void greetings() {

        // given
        classUnderTest.setup(buildConfig(GreetingSetup.LocaleType.LOCALE_UNSET));

        // when
        var response = classUnderTest.greetings(fluxRequestBuilder("Jason","Odin","Thor"),null);

        // then
        StepVerifier.create(response.map(GreetingResponse::getGreeting))
                .expectNext("Hello Jason","Hello Odin","Hello Thor")
                .verifyComplete();

    }

    @Test
    void logGreeting() {

        // given
        var request = GreetingRequest.newBuilder().setName(TEST_NAME).build();

        // when
        var response = classUnderTest.logGreeting(request,null).block();


        // then
        assertEquals( Empty.newBuilder().build(),response);
       /* assertThat(loggingEventListAppender.list)
                .extracting(ILoggingEvent::getMessage)
                .contains("Greeting fire and forget request received");*/
    }

    private GreetingSetup buildConfig(GreetingSetup.LocaleType locale){
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        return  GreetingSetup.newBuilder()
                .setLocale(locale)
                .setInitiated(timestamp)
                .build();


    }
    private GreetingRequest requestBuilder(String name) {
        return GreetingRequest.newBuilder().setName(name).build();
    }

    private Flux<GreetingRequest> fluxRequestBuilder(String... names){
        var greetings = new ArrayList<GreetingRequest>();
        for (String name:names) {
            greetings.add(requestBuilder(name));
        }
        return Flux.fromIterable(greetings);
    }

}