package jpdemo.reactivegreeting.rsocketgreeting.config;

import jpdemo.proto.greeting.v1.GreetingRequest;
import jpdemo.proto.greeting.v1.GreetingResponse;
import jpdemo.proto.greeting.v1.RandomGreetingRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={RSocketServerConfig.class}, loader= AnnotationConfigContextLoader.class)
/**
 * Tests beans loaded correctly
 */
class RSocketServerConfigTest {

    /**
     * Test handler bean can be accessed and is not null
     * @param messageHandler: RSocketMessageHandler config bean
     */
    @Test
    void rSocketMessageHandler(@Autowired RSocketMessageHandler messageHandler) {
        assertNotNull(messageHandler, "Message handler bean should have loaded.");
        assertNotNull(messageHandler.getRSocketStrategies());
    }

    /**
     * Test bean is loaded, and bean contains required strategies
     * @param strategies: RSocketStrategies bean to test
     */
    @Test
    void rSocketStrategies(@Autowired RSocketStrategies strategies) {
        assertNotNull(strategies," Strategies handler bean should have loaded");
        assertNotNull(strategies.decoder(ResolvableType.forType(GreetingRequest.class), null), "Has a protobuf decoder");
        assertNotNull(strategies.decoder(ResolvableType.forType(GreetingResponse.class), null), "Has a protobuf decoder");
        assertNotNull(strategies.decoder(ResolvableType.forType(RandomGreetingRequest.class), null), "Has a protobuf decoder");
        assertNotNull(strategies.encoder(ResolvableType.forType(RandomGreetingRequest.class), null), "Has a protobuf decoder");
        assertEquals(ProtobufDecoder.class, (strategies.decoder(ResolvableType.forType(GreetingRequest.class), null)).getClass(), "Has a protobuf decoder");
        assertEquals(ProtobufEncoder.class, (strategies.encoder(ResolvableType.forType(GreetingRequest.class), null)).getClass(), "Has a protobuf encoder");
    }


}