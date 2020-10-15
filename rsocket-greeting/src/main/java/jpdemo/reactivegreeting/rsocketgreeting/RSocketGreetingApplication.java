package jpdemo.reactivegreeting.rsocketgreeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "jpdemo.reactivegreeting")
public class RSocketGreetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RSocketGreetingApplication.class, args);
	}

}
