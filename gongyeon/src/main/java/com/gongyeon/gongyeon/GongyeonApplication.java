package com.gongyeon.gongyeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GongyeonApplication {

	public static void main(String[] args) {
		SpringApplication.run(GongyeonApplication.class, args);
	}

}
