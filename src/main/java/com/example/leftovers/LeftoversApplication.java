package com.example.leftovers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@SpringBootApplication
public class LeftoversApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeftoversApplication.class, args);
	}

}
