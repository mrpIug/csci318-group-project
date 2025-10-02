package com.group18.rotionary.rotlegame;

import io.github.cdimascio.dotenv.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RotleGameServiceApplication {

	public RotleGameServiceApplication() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	public static void main(String[] args) {
		new RotleGameServiceApplication();
		SpringApplication.run(RotleGameServiceApplication.class, args);
	}

}
