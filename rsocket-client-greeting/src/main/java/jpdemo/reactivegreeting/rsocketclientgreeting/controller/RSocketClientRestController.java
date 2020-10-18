package jpdemo.reactivegreeting.rsocketclientgreeting.controller;

import jpdemo.reactivegreeting.rsocketclientgreeting.adaptor.GreetingAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RSocketClientRestController {

    private final GreetingAdaptor adaptor;

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

   /* @GetMapping("log")
    public void greetingLog(@RequestParam(value = "name", defaultValue = "World") String name){
        adaptor.
        greetingService.logGreeting(request,null);
    }

    @GetMapping("stream")
    public Flux<String> randomGreetings(){
        return randomGreetingService.randomGreetings(null,null).map(response -> (response.getGreeting()+"\n"));
    }

    @GetMapping("random")
    public Mono<String> randomGreeting(){
        return randomGreetingService.randomGreeting(null,null).map(response -> response.getGreeting());
    }*/


}
