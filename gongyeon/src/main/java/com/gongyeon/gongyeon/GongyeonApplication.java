package com.gongyeon.gongyeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GongyeonApplication {

	public static void main(String[] args) {
		SpringApplication.run(GongyeonApplication.class, args);
	}

	@Bean
	public FlywayMigrationStrategy cleanMigrationStrategy(){
		return flyway -> {
			flyway.repair();
			flyway.migrate();
		};
	}

}
