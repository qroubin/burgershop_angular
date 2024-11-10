package com.burgers.burgers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class BurgersApplication {

	public static void main(String[] args) {
		SpringApplication.run(BurgersApplication.class, args);
	}

}
