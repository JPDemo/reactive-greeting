package jpdemo.reactivegreeting.rsocketgreeting.controller;


import com.google.protobuf.Empty;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final DefaultGreetingService greetingService;
    private final DefaultRandomGreetingService randomGreetingService;

    @MessageMapping("greeting.request")
    public Mono<GreetingResponse> greetingRequestResponse(GreetingRequest request){
        return greetingService.greeting(request,null);
    }

    @MessageMapping("greeting.channel")
    public Flux<GreetingResponse> greetingChannel(Publisher<GreetingRequest> requests){
        return greetingService.greetings(requests,null);
    }

    @MessageMapping("greeting.fireforget")
    public Mono<Empty> greetingLog(GreetingRequest request){
        return greetingService.logGreeting(request,null);
    }

    @MessageMapping("greeting.stream")
    public Flux<GreetingResponse> randomGreetings(){
        return randomGreetingService.randomGreetings(null,null);
    }

    @MessageMapping("greeting.random")
    public Mono<GreetingResponse> randomGreeting(){
        return randomGreetingService.randomGreeting(null,null);
    }

}
