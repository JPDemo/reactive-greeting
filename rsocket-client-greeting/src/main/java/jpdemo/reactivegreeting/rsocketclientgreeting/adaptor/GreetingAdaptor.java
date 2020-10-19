package jpdemo.reactivegreeting.rsocketclientgreeting.adaptor;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class GreetingAdaptor {


    private final RSocketRequester rSocketRequester;

    /**
     * Greeting request / response
     * Request: mono
     * Response: mono
     *
     * @param name
     * @return
     */
    public Mono<String> greetingRequestResponse(String name) {
        log.info("Adaptor - Request response received with name {}", name);
        return rSocketRequester
                .route("greeting.request")
                .data(requestBuilder(name))
                .retrieveMono(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());
    }


    /**
     * Greeting channel
     * Produce data for channel request
     *
     * @return
     */
    public Flux<String> greetingChannel() {
        log.info("Adaptor - Channel request received");
        return greetingChannel(
                Flux
                        .interval(Duration.ofMillis(10000))
                        .map(request -> requestBuilder(randomNameGenerator())));
    }

    /**
     * Greeting channel
     * Request: flux
     * Response: flux
     *
     * @param requests
     * @return
     */
    public Flux<String> greetingChannel(Flux<GreetingRequest> requests) {
        return rSocketRequester
                .route("greeting.channel")
                .data(requests)
                .retrieveFlux(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());
    }

    /**
     * Greeting fire and forget example - a log in this instance
     * Request: mono
     * Response: null
     *
     * @param name
     */
    public void logGreeting(String name) {
        log.info("Adaptor - Log Request response log received with name {}", name);
        rSocketRequester
                .route("greeting.fireforget")
                .data(requestBuilder(name))
                .send()
                .doOnSuccess(s -> log.info("Success {}", s))
                .doOnError(e -> log.info("Error {}", e))

        ;
    }


    /**
     * Greeting stream
     * Returns random stream of greetings
     * Request: null
     * Response: flux
     *
     * @return
     */
    public Flux<String> randomGreetingsStream() {
        log.info("Adaptor - random greeting stream request received");
        return rSocketRequester
                .route("greeting.stream")
                //.data(requests)
                .retrieveFlux(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());
    }

    /**
     * Random greeting response
     * Request: null
     * Response: mono
     *
     * @return
     */
    public Mono<String> randomGreetingRequest() {
        log.info("Adaptor - random greeting request received");
        return rSocketRequester
                .route("greeting.random")
                //.data(requestBuilder(name))
                .retrieveMono(GreetingResponse.class)
                .log()
                .map(response -> response.getGreeting());
    }

    /**
     * Helper method to construct GreetingRequest object from a name
      * @param name
     * @return
     */
    private GreetingRequest requestBuilder(String name) {
        return GreetingRequest.newBuilder().setName(name).build();
    }

    /**
     * Helper method to generate random names
     * @return
     */
    private String randomNameGenerator() {
        var names = Arrays.asList("Odin", "Thor", "Frigg", "Balder", "Loki", "Freya");
        return names.get(ThreadLocalRandom.current().nextInt(0, names.size() - 1));
    }


}
