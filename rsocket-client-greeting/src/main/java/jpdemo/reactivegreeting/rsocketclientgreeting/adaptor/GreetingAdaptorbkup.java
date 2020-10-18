package jpdemo.reactivegreeting.rsocketclientgreeting.adaptor;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Slf4j
public class GreetingAdaptorbkup {

    //private final Mono<RSocketRequester> rSocketRequester;
    private final RSocketRequester rSocketRequester;
    private final GreetingServiceClient greetingServiceClient;

    public Mono<String> greetingRequestResponse(String name) {
        log.info("Adaptor - Request response received with name {}",name);
        return rSocketRequester
                .route("greeting.request").data(requestBuilder(name))
                .retrieveMono(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());


    }

/*    public Mono<String> greetingRequestResponse(String name) {
        log.info("Adaptor - Request response received with name {}",name);
        return rSocketRequester
                .map(r -> r.route("greeting.request").data(requestBuilder(name)))
                .flatMap(r -> r.retrieveMono(GreetingResponse.class))
                .log()
                .map(response -> response.getGreeting());


    }*/


    public Flux<String> greetingChannel() {
        log.info("Adaptor - Channel request received");
        return greetingChannel(
                Flux
                .interval(Duration.ofMillis(10000))
                .map(request -> requestBuilder(randomNameGenerator())));
    }

/*    public Flux<String> greetingChannel(Flux<GreetingRequest> requests) {
        return rSocketRequester
                 .map( r-> r.route("greeting.channel")
                .data(requests))
                .flatMapMany( r-> r.retrieveFlux(GreetingResponse.class))
                .log()
                .map(r -> r.getGreeting());
    }*/

    public Flux<String> greetingChannel(Flux<GreetingRequest> requests) {
        return rSocketRequester
                .route("greeting.channel")
                .data(requests)
                .retrieveFlux(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());
    }


    private GreetingRequest requestBuilder(String name) {
        return GreetingRequest.newBuilder().setName(name).build();
    }

    private String randomNameGenerator() {
        var names = Arrays.asList("Jason", "Nicola", "Sophie", "Marianne", "Bob", "John");
        return names.get(ThreadLocalRandom.current().nextInt(0, names.size() - 1));
    }


}
