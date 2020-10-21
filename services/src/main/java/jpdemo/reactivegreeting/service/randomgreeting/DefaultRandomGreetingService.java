package jpdemo.reactivegreeting.service.randomgreeting;

import io.netty.buffer.ByteBuf;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.RandomGreetingRequest;
import jpdemo.proto.greeting.v1.RandomGreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DefaultRandomGreetingService implements RandomGreetingService {

    @Override
    public Flux<GreetingResponse> randomGreetings(RandomGreetingRequest randomGreetingRequest, ByteBuf byteBuf) {
        log.info("Random greeting stream request received");
        return Flux.interval(Duration.ofSeconds(5)).map(response -> GreetingResponse.newBuilder().setGreeting(randomGreeting()).build()).log();
    }

    @Override
    public Mono<GreetingResponse> randomGreeting(RandomGreetingRequest randomGreetingRequest, ByteBuf byteBuf) {
        log.info("Random greeting request-response received");
        return Mono.just(GreetingResponse.newBuilder().setGreeting(randomGreeting()).build()).log();
    }

    private String randomGreeting() {
        return String.format("%s %s!", randomGreetingGenerator(), randomNameGenerator());
    }

    private String randomNameGenerator() {
        var names = Arrays.asList("Odin", "Thor", "Frigg", "Balder", "Loki", "Freya");
        return names.get(ThreadLocalRandom.current().nextInt(0, names.size() - 1));
    }

    private String randomGreetingGenerator() {
        var names = Arrays.asList("Hi", "Hello", "Hola", "Sawabona", "Hey");
        return names.get(ThreadLocalRandom.current().nextInt(0, names.size() - 1));
    }

}
