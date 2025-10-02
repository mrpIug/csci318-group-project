package com.group18.rotionary.agenticai;

import io.github.cdimascio.dotenv.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgenticAiServiceApplication {

	public AgenticAiServiceApplication() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	public static void main(String[] args) {
		new AgenticAiServiceApplication();
		SpringApplication.run(AgenticAiServiceApplication.class, args);
	}

}
