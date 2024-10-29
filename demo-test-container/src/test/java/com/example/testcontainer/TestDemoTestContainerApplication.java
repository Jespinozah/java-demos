package com.example.testcontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestDemoTestContainerApplication {

	@Bean
	@RestartScope
	@ServiceConnection
	PostgreSQLContainer postgreSQLContainer(){
		return new PostgreSQLContainer("postgres:15-alpine");
	}

	public static void main(String[] args) {
		SpringApplication.from(DemoTestContainerApplication::main)
				.with(TestDemoTestContainerApplication.class).run(args);
	}

}
