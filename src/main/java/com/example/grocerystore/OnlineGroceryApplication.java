package com.example.grocerystore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.example.grocerystore")
@EntityScan
public class OnlineGroceryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineGroceryApplication.class, args);
	}

}
