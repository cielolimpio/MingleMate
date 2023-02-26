package com.mingleMate.mingleMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MingleMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MingleMateApplication.class, args);
	}

	@Bean
	public FlywayMigrationStrategy cleanMigrationStrategy(){
		return flyway -> {
			flyway.repair();
			flyway.migrate();
		};
	}

}
