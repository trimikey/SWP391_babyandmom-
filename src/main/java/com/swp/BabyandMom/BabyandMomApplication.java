package com.swp.BabyandMom;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
		info = @Info(
				title = "Baby and Mom API",
				version = "1.0",
				description = "API Documentation for Baby and Mom Application"
		)
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
public class BabyandMomApplication {
	private static final Logger logger = LoggerFactory.getLogger(BabyandMomApplication.class);

	public static void main(String[] args) {
		logger.info("Starting application...");
		SpringApplication.run(BabyandMomApplication.class, args);
		logger.info("Application started successfully!");
	}
}
