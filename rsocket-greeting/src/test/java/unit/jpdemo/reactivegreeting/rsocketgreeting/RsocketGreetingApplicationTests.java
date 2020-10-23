package jpdemo.reactivegreeting.rsocketgreeting;

import jpdemo.reactivegreeting.service.greeting.DefaultGreetingService;
import jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@Import({jpdemo.reactivegreeting.service.greeting.DefaultGreetingService.class,jpdemo.reactivegreeting.service.randomgreeting.DefaultRandomGreetingService.class})
@SpringBootTest(classes = {DefaultGreetingService.class, DefaultRandomGreetingService.class})
class RsocketGreetingApplicationTests {

	@Test
	void contextLoads() {
	}

}
