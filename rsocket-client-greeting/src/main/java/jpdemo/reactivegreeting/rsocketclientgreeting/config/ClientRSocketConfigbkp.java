package jpdemo.reactivegreeting.rsocketclientgreeting.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDate;

@Configuration
@Slf4j
public class ClientRSocketConfigbkp {

    @Value("${jpdemo.rsocket-greeting.service.hostname}")
    private String helloServiceHostname;

    @Value("${jpdemo.rsocket-greeting.service.port}")
    private int helloServicePort;


    RSocketRequester connectToGreetingService() {
        LocalDate timestamp = LocalDate.now();
        log.info("Connecting to greeting service with connection timestamp of {}",timestamp);

        return RSocketRequester.builder()
                //.setupRoute("greeting.setup")
                //.setupData(timestamp)
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

   /* @Bean
    RSocketRequester connectToGreetingService() {
        LocalDate timestamp = LocalDate.now();
        log.info("Connecting to greeting service with connection timestamp of {}",timestamp);

        return RSocketRequester.builder()
                .setupRoute("greeting.setup")
                .setupData(timestamp)
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .rsocketStrategies(builder -> {
                    builder.encoder(new Jackson2JsonEncoder());
                    builder.encoder(new Jackson2CborEncoder());
                })
                .connectTcp(helloServiceHostname, helloServicePort)
                .block();
    }*/
/*
    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();
    }

    @Bean
    public Mono<RSocketRequester> getRSocketRequester(RSocketRequester.Builder builder){

        LocalDate timestamp = LocalDate.now();
        log.info("Connecting to greeting service with connection timestamp of {}",timestamp);

        return builder
                .setupRoute("greeting.setup")
                .setupData(timestamp)
                .rsocketConnector(rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MediaType.APPLICATION_CBOR)
                .connectTcp(helloServiceHostname, helloServicePort);

    }*/
/*
    RSocketRequester connectToGreetingService() {
        LocalDate timestamp = LocalDate.now();
        log.info("Connecting to greeting service with connection timestamp of {}",timestamp);

        return RSocketRequester.builder()
                .setupRoute("greeting.setup")
                .setupData(timestamp)
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .rsocketStrategies(builder -> {
                    builder.encoder(new Jackson2JsonEncoder());
                    builder.encoder(new Jackson2CborEncoder());
                })
                .connectTcp(helloServiceHostname, helloServicePort)
                .block();
    }*/



}
