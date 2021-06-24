package com.example.DataService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.repository")
@EntityScan("model")
@EnableJpaRepositories("com.example.repository")
@ComponentScan("com.example")
public class DataServiceApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(DataServiceApplication.class, args);
		
		
		
	}

}
