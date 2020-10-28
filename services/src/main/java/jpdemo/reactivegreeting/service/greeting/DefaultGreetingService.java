package jpdemo.reactivegreeting.service.greeting;

import com.google.protobuf.Empty;
import io.netty.buffer.ByteBuf;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingService;
import jpdemo.proto.greeting.v1.GreetingSetup;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DefaultGreetingService implements GreetingService {


    private GreetingSetup config;
    private GreetingSetup.LocaleType locale = GreetingSetup.LocaleType.LOCALE_UNSET;

    public void setup(GreetingSetup setup) {
        log.info("Config setup invoked with {}", setup.toString());
        config = setup;
        locale = setup.getLocale();
    }

    public GreetingSetup getConfig() {
        return config;
    }

    /***
     * Greeting request-response
     * @param greetingRequest: a greeting request
     * @param byteBuf: not used
     * @return : Mono GreetingResponse
     */
    @Override
    public Mono<GreetingResponse> greeting(GreetingRequest greetingRequest, ByteBuf byteBuf) {
        log.info("Greeting request-response received: {} buffer {}", greetingRequest, byteBuf);
        return Mono.just(GreetingResponse.newBuilder().setGreeting(getGreeting() + greetingRequest.getName()).build()).log();
    }

    /***
     * Greetings channel
     * @param requests: a flux greeting request
     * @param byteBuf: not used
     * @return : Flux GreetingResponse
     */
    @Override
    public Flux<GreetingResponse> greetings(Publisher<GreetingRequest> requests, ByteBuf byteBuf) {
        return Flux.from(requests).doOnNext(request -> log.info("Received greetings channel request [{}]", request)).map(request -> GreetingResponse.newBuilder().setGreeting(getGreeting() + request.getName()).build());
    }

    /***
     * Greeting fire and forget
     * @param greetingRequest: a greeting request
     * @param byteBuf: not used
     * @return : Mono<Empty>
     */
    @Override
    public Mono<Empty> logGreeting(GreetingRequest greetingRequest, ByteBuf byteBuf) {
        log.info("Greeting fire and forget request received: {} buffer {}", greetingRequest, byteBuf);
        log.info(getGreeting() + greetingRequest.getName());
        return Mono.just(Empty.newBuilder().build());
    }

/*    private String getGreeting() {

        String greeting ;
        switch (locale) {
            case LOCALE_EN -> greeting = "Hi ";
            case LOCALE_DE -> greeting = "Guten tag ";
            case LOCALE_ES -> greeting = "Hola ";
            default -> greeting = "Hello ";
        }

        return greeting;
    }*/

    private String getGreeting() {

        String greeting;
        switch (locale) {
            case LOCALE_EN:
                greeting = "Hi ";
                break;
            case LOCALE_DE:
                greeting = "Guten tag ";
                break;
            case LOCALE_ES:
                greeting = "Hola ";
                break;
            default:
                greeting = "Hello ";

        }
        return greeting;
    }
}
