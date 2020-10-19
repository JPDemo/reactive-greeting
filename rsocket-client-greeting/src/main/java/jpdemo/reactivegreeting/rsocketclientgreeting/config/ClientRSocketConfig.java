package jpdemo.reactivegreeting.rsocketclientgreeting.config;

import com.google.protobuf.Timestamp;
import jpdemo.proto.greeting.v1.GreetingSetup;
import lombok.RequiredArgsConstructor;
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

import java.time.Instant;

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
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();

        var config = GreetingSetup.newBuilder()
                .setLocale(GreetingSetup.LocaleType.LOCALE_EN)
                .setInitiated(timestamp)
                .build();
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
