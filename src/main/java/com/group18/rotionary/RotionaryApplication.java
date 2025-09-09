package com.group18.rotionary;

import io.github.cdimascio.dotenv.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RotionaryApplication {

	public RotionaryApplication() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	public static void main(String[] args) {
		new RotionaryApplication();
		SpringApplication.run(RotionaryApplication.class, args);
	}

}
