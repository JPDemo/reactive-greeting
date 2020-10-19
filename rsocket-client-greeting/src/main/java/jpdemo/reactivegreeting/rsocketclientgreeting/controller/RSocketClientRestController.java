package jpdemo.reactivegreeting.rsocketclientgreeting.controller;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingServiceClient;
import jpdemo.reactivegreeting.rsocketclientgreeting.adaptor.GreetingAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RSocketClientRestController {

    private final GreetingAdaptor adaptor;
    private final GreetingServiceClient greetingServiceClient;

    @GetMapping("")
    public String connection(){
        return "Welcome to greeting rsocket rest client";
    }

    @GetMapping("request")
    public Mono<String> greetingRequestResponse(@RequestParam(value = "name", defaultValue = "World") String name){

        return adaptor.greetingRequestResponse(name);
    }

   /* @GetMapping("channel")
    public Flux<String> greetingChannel(){
        return adaptor.greetingChannel();
    }*/

    @GetMapping("log")
    public void greetingLog(@RequestParam(value = "name", defaultValue = "World") String name){
        log.info("Greeting service log request response received with name {}",name);
        var request = GreetingRequest.newBuilder().setName(name).build();
        greetingServiceClient.logGreeting(request,null);
    }
  /*
    @GetMapping("stream")
    public Flux<String> randomGreetings(){
        return randomGreetingService.randomGreetings(null,null).map(response -> (response.getGreeting()+"\n"));
    }

    @GetMapping("random")
    public Mono<String> randomGreeting(){
        return randomGreetingService.randomGreeting(null,null).map(response -> response.getGreeting());
    }*/


}
