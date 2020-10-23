package jpdemo.reactivegreeting.restgreeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"jpdemo.reactivegreeting.restgreeting","jpdemo.reactivegreeting.service"})
public class RestGreetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestGreetingApplication.class, args);
	}

}
