package jpdemo.reactivegreeting.rsocketclientgreeting.config;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.frame.decoder.ZeroCopyPayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingServiceClient;
import jpdemo.proto.greeting.v1.GreetingSetup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ClientRSocketConfig {

    @Value("${jpdemo.rsocket-greeting.service.hostname}")
    private String helloServiceHostname;

    @Value("${jpdemo.rsocket-greeting.service.port}")
    private int helloServicePort;



    @Bean
    RSocketRequester connectToGreetingService() {
        var config = GreetingSetup.newBuilder().setLocale(GreetingSetup.LocaleType.LOCALE_EN).build();
        log.info("Connecting to greeting service with config: {}",config.toString());

        return RSocketRequester.builder()
                .setupRoute("greeting.setup")
                .setupData(config)
                .dataMimeType(MimeTypeUtils.ALL)
                .rsocketStrategies(builder -> {
                    builder.encoder(new ProtobufEncoder());
                    builder.encoder(new Jackson2CborEncoder());
                    builder.encoder(new Jackson2JsonEncoder());
                    builder.decoder(new ProtobufDecoder());
                })
                .connectTcp(helloServiceHostname, helloServicePort)
                .block();
    }




}
