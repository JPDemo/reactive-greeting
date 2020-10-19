package jpdemo.reactivegreeting.rsocketgreeting.config;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import io.rsocket.transport.netty.server.TcpServerTransport;
import jpdemo.proto.greeting.v1.GreetingServiceServer;
import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class RSocketServerConfig {

 /*   private final RSocket rSocket;
    private final GreetingServiceServer serviceServer;*/

    @Bean
    public RSocketMessageHandler rsocketMessageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(rSocketStrategies);

        return handler;
    }


    @Bean
    public RSocketStrategies rsocketStrategies() {
        return RSocketStrategies.builder()
                .decoders(decoders -> {
                    decoders.add((new ProtobufDecoder()));
                    decoders.add(new Jackson2JsonDecoder());
                    decoders.add(new Jackson2CborDecoder());
                }).encoders( encoders -> {
                    encoders.add(new ProtobufEncoder());
                    encoders.add(new Jackson2CborEncoder());
                    encoders.add(new Jackson2JsonEncoder());
                    encoders.add(new ProtobufEncoder());
        })
                .build();
    }


    GreetingServiceServer greetingServiceServer(){
        var server = new GreetingServiceServer(new DefaultGreetingService(),null,null);

        return server;
    }


}
