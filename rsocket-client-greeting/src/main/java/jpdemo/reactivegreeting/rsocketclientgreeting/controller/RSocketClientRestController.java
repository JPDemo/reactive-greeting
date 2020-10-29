package jpdemo.reactivegreeting.rsocketclientgreeting.controller;

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


    @GetMapping("")
    public String connection(){
        return "Welcome to greeting rsocket rest client";
    }

    @GetMapping("/")
    public String connection2(){
        return "Welcome to greeting rsocket rest client /endpoint";
    }

    @GetMapping("request")
    public Mono<String> greetingRequestResponse(@RequestParam(value = "name", defaultValue = "World") String name){

        return adaptor.greetingRequestResponse(name);
    }

   @GetMapping("channel")
    public Flux<String> greetingChannel(){
        return adaptor.greetingChannel();
    }

    @GetMapping("log")
    public void greetingLog(@RequestParam(value = "name", defaultValue = "World") String name){
        log.info("Greeting service log request response received with name {}",name);
        adaptor.logGreeting(name);
    }

    @GetMapping("stream")
    public Flux<String> randomGreetings(){
        return adaptor.randomGreetingsStream();
    }

    @GetMapping("random")
    public Mono<String> randomGreeting(){
        return adaptor.randomGreetingRequest();
    }


}
