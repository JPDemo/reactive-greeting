package jpdemo.reactivegreeting.rsocketgreeting.controller;

import com.google.protobuf.Timestamp;
import io.rsocket.metadata.WellKnownMimeType;
import jpdemo.proto.context.v1.MessageContext;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingSetup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GreetingControllerITest {

    private static Mono<RSocketRequester> monoRequester;
    private final static String NAME = "Jason";

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder, @Value("${spring.rsocket.server.port}") Integer port) {
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();

        var config = GreetingSetup.newBuilder()
                .setLocale(GreetingSetup.LocaleType.LOCALE_EN)
                .setInitiated(timestamp)
                .build();

        monoRequester = builder
                .setupRoute("greeting.setup")
                .setupData(config)
                .rsocketConnector(connector -> connector.reconnect(Retry.backoff(10, Duration.ofMillis(500))))
                .dataMimeType(MimeTypeUtils.ALL)
                .rsocketStrategies(rsBuilder -> {
                    rsBuilder.encoder(new ProtobufEncoder());
                    rsBuilder.decoder(new ProtobufDecoder());
                })
                .connectTcp("localhost", port);


    }

    @Test
    void setup() {
    }

    @Test
    void greetingRequestResponseMetaDataTest() {

        var messageContext = MessageContext.newBuilder()
                .setInitiated(Timestamp.newBuilder().build())
                .setOrigin("Integration Test")
                .setRequestId(UUID.randomUUID().toString())
                .build();


    /*    var config = GreetingSetup.newBuilder()
                .setLocale(GreetingSetup.LocaleType.LOCALE_DE)
                .build();*/
        var request = GreetingRequest.newBuilder().setName(NAME).build();
        var response = monoRequester.flatMap(requester ->
                requester
                        .route("greeting.request")
                        //.metadata(config, MimeType.valueOf("application/x-protobuf"))
                        .metadata(messageContext, MimeType.valueOf("application/x-protobuf"))
                        .data(request)
                        .retrieveMono(GreetingResponse.class));

        // Verify that the response message contains the expected data (2)
        StepVerifier
                .create(response)
                .consumeNextWith(greetingResponse -> {
                    assertThat(greetingResponse.getGreeting()).isEqualTo("Hi " + NAME);
                })
                .verifyComplete();

    }

    @Test
    void greetingRequestResponseWithoutMetaData() {

              var request = GreetingRequest.newBuilder().setName(NAME).build();
        var response = monoRequester.flatMap(requester ->
                requester
                        .route("greeting.request")
                        .data(request)
                        .retrieveMono(GreetingResponse.class));

        // Verify that the response message contains the expected data (2)
        StepVerifier
                .create(response)
                .consumeNextWith(greetingResponse -> {
                    assertThat(greetingResponse.getGreeting()).isEqualTo("Hi " + NAME);
                })
                .verifyComplete();

    }

    @Test
    void testGreetingRequestResponse() {
    }

    @Test
    void greetingChannel() {
    }

    @Test
    void greetingLog() {
    }

    @Test
    void randomGreetings() {
    }

    @Test
    void randomGreeting() {
    }
}