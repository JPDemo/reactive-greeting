package jpdemo.reactivegreeting.rsocketclientgreeting;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RSocketClientGreetingApplicationTests {

	@MockBean
	private RSocketRequester rSocketRequester; // mock bean so connection not attempted


	@Test
	void contextLoads() {

	}

}
