package jpdemo.reactivegreeting.rsocketgreeting.config;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import io.rsocket.transport.netty.server.TcpServerTransport;
import jpdemo.proto.context.v1.MessageContext;
import jpdemo.proto.greeting.v1.GreetingServiceServer;
import jpdemo.proto.greeting.v1.GreetingSetup;
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
import org.springframework.messaging.rsocket.DefaultMetadataExtractor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class RSocketServerConfig {


    @Bean
    public RSocketMessageHandler rsocketMessageHandler(RSocketStrategies rSocketStrategies) {
        var protoMimeType = MimeType.valueOf("application/x-protobuf");
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(rSocketStrategies);
        var metadataExtractor = new DefaultMetadataExtractor(new ProtobufDecoder());
        metadataExtractor.metadataToExtract(protoMimeType,  MessageContext.class,"messageContext");
      //  metadataExtractor.metadataToExtract(protoMimeType,  GreetingSetup.class,"greetingSetup");

        handler.setMetadataExtractor(metadataExtractor);
        return handler;
    }


    @Bean
    public RSocketStrategies rsocketStrategies() {
        return RSocketStrategies.builder()
                .decoders(decoders -> {
                    decoders.add((new ProtobufDecoder()));
                }).encoders( encoders -> {
                    encoders.add(new ProtobufEncoder());
        })
                .build();
    }



}
