package jpdemo.reactivegreeting.restgreeting.controller;

import com.google.protobuf.Empty;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@RestController
@RequestMapping("/greeting")
public class GreetingRestController {

    private final DefaultGreetingService greetingService;
    private final DefaultRandomGreetingService randomGreetingService;

    /***
     * http://localhost:8080/greeting/request
     * Hello World
     * http://localhost:8080/greeting/request?name=User
     * Hello User
     * @param name
     * @return
     */
    @GetMapping("request")
    public Mono<String> greetingRequestResponse(@RequestParam(value = "name", defaultValue = "World") String name){

        var request = GreetingRequest.newBuilder().setName(name).build();

        return greetingService.greeting(request,null).map(response -> response.getGreeting());
    }

    @GetMapping("channel")
    public Flux<GreetingResponse> greetingChannel(Publisher<GreetingRequest> requests){
        return greetingService.greetings(requests,null);
    }

    @GetMapping("log")
    public void greetingLog(@RequestParam(value = "name", defaultValue = "World") String name){
        var request = GreetingRequest.newBuilder().setName(name).build();

        greetingService.logGreeting(request,null);
    }

    @GetMapping("stream")
    public Flux<String> randomGreetings(){
        return randomGreetingService.randomGreetings(null,null).map(response -> (response.getGreeting()+"\n"));
    }

    @GetMapping("random")
    public Mono<String> randomGreeting(){
        return randomGreetingService.randomGreeting(null,null).map(response -> response.getGreeting());
    }
}
