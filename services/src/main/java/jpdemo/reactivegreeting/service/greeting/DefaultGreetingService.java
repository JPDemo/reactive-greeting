package jpdemo.reactivegreeting.service.greeting;

import com.google.protobuf.Empty;
import io.netty.buffer.ByteBuf;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DefaultGreetingService implements GreetingService {

    /***
     * Greeting request-response
     * @param greetingRequest
     * @param byteBuf
     * @return
     */
    @Override
    public Mono<GreetingResponse> greeting(GreetingRequest greetingRequest, ByteBuf byteBuf) {
        log.info("Greeting request-response received: {} buffer {}", greetingRequest, byteBuf);
        return Mono.just(GreetingResponse.newBuilder().setGreeting("Hello " + greetingRequest.getName()).build()).log();
    }

    /***
     * Greetings channel
     * @param requests
     * @param byteBuf
     * @return
     */
    @Override
    public Flux<GreetingResponse> greetings(Publisher<GreetingRequest> requests, ByteBuf byteBuf) {
        return Flux.from(requests).doOnNext(request -> log.info("Received greetings channel request [{}]", request)).map(request -> GreetingResponse.newBuilder().setGreeting("Hello " + request.getName()).build());
    }

    /***
     * Greeting fire and forget
     * @param greetingRequest
     * @param byteBuf
     * @return
     */
    @Override
    public Mono<Empty> logGreeting(GreetingRequest greetingRequest, ByteBuf byteBuf) {
        log.info("Greeting fire and forget request received: {} buffer {}", greetingRequest, byteBuf);
        log.info("Hello " + greetingRequest.getName());
        return Mono.just(Empty.newBuilder().build());
    }
}
