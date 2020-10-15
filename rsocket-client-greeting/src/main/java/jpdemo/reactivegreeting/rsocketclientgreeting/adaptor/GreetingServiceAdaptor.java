package jpdemo.reactivegreeting.rsocketclientgreeting.adaptor;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class GreetingServiceAdaptor {

    private final RSocketRequester rSocketRequester;

    public Mono<String> greetingRequestResponse(String name) {
        return rSocketRequester
                .route("greeting.request")
                .data(requestBuilder(name))
                .retrieveMono(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());

    }

    public Flux<String> greetingChannel() {
        return greetingChannel(
                Flux
                .interval(Duration.ofMillis(10000))
                .map(request -> requestBuilder(randomNameGenerator())));
    }

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
