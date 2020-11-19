package jpdemo.reactivegreeting.rsocketgreeting.controller;


import com.google.protobuf.Empty;
import jpdemo.proto.context.v1.MessageContext;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.GreetingSetup;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@Slf4j
public class GreetingController {

    private final DefaultGreetingService greetingService;
    private final DefaultRandomGreetingService randomGreetingService;

    private LocalDateTime connectionInitiation;  // value is assigned during SETUP frame

    @ConnectMapping("greeting.setup")
    public void setup(GreetingSetup config) {

        Assert.notNull(config, "Config should not be null");
        greetingService.setup(config);
        var tStamp = config.getInitiated();
        connectionInitiation = Instant
                .ofEpochSecond(tStamp.getSeconds(), tStamp.getNanos())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @MessageMapping({"greeting.request"})
    public Mono<GreetingResponse> greetingRequestResponse(@Headers Map<String, Object> metadata, GreetingRequest request) {

        log.info("Connection start timestamp " + connectionInitiation);
        var messageContext = metadata != null ? (MessageContext) metadata.get("messageContext") : null;
        if (messageContext != null) {
            log.info("Retrieved meta data " + messageContext);
        } else {
            log.info("No message context meta data supplied");
        }

        return greetingService.greeting(request, null);
    }

    @MessageMapping("greeting.channel")
    public Flux<GreetingResponse> greetingChannel(Publisher<GreetingRequest> requests) {
        log.info("Connection start timestamp " + connectionInitiation);
        return greetingService.greetings(requests, null);
    }

    @MessageMapping("greeting.fireforget")
    public Mono<Empty> greetingLog(GreetingRequest request) {
        log.info("Connection start timestamp " + connectionInitiation);
        return greetingService.logGreeting(request, null);
    }

    @MessageMapping("greeting.stream")
    public Flux<GreetingResponse> randomGreetings() {
        log.info("Connection start timestamp " + connectionInitiation);
        return randomGreetingService.randomGreetings(null, null);
    }

    @MessageMapping("greeting.random")
    public Mono<GreetingResponse> randomGreeting() {
        log.info("Connection start timestamp " + connectionInitiation);
        return randomGreetingService.randomGreeting(null, null);
    }

}
