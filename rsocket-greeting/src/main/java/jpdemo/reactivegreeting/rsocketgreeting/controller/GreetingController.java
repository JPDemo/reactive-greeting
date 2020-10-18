package jpdemo.reactivegreeting.rsocketgreeting.controller;


import com.google.protobuf.Empty;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


@Controller
@RequiredArgsConstructor
@Slf4j
public class GreetingController {

    private final DefaultGreetingService greetingService;
    private final DefaultRandomGreetingService randomGreetingService;

    private LocalDate connectionInitiation;  // value is assigned during SETUP frame
    private String intString;  // value is assigned during SETUP frame

    @ConnectMapping("greeting.setup")
    public void setup(LocalDate timestamp){
        connectionInitiation = timestamp;
        log.info("Setup complete -  start timestamp "+connectionInitiation);
    }

    @ConnectMapping("greeting.setup2")
    public void setup2(GreetingRequest request){
        intString = request.getName();
        log.info("Setup complete -  init val "+intString);
    }

    @MessageMapping("greeting.request")
    public Mono<GreetingResponse> greetingRequestResponse(GreetingRequest request){
        log.info("Connection start timestamp "+connectionInitiation);
        return greetingService.greeting(request,null);
    }

    @MessageMapping("greeting.channel")
    public Flux<GreetingResponse> greetingChannel(Publisher<GreetingRequest> requests){
        log.info("Connection start timestamp "+connectionInitiation);
        return greetingService.greetings(requests,null);
    }

    @MessageMapping("greeting.fireforget")
    public Mono<Empty> greetingLog(GreetingRequest request){
        log.info("Connection start timestamp "+connectionInitiation);
        return greetingService.logGreeting(request,null);
    }

    @MessageMapping("greeting.stream")
    public Flux<GreetingResponse> randomGreetings(){
        log.info("Connection start timestamp "+connectionInitiation);
        return randomGreetingService.randomGreetings(null,null);
    }

    @MessageMapping("greeting.random")
    public Mono<GreetingResponse> randomGreeting(){
        log.info("Connection start timestamp "+connectionInitiation);
        return randomGreetingService.randomGreeting(null,null);
    }

}
