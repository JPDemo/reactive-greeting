package jpdemo.reactivegreeting.rsocketclientgreeting.config;

import jpdemo.proto.greeting.v1.GreetingServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

@Configuration
@RequiredArgsConstructor
public class GreetungServiceClientConfig {

    private final RSocketRequester requester;

    @Bean
    GreetingServiceClient GreetingServiceClient( ){
        var greetingServiceClient = new GreetingServiceClient(requester.rsocket());
        return greetingServiceClient;
    }


}
