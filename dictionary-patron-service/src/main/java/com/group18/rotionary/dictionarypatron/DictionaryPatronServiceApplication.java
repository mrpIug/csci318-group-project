package com.group18.rotionary.dictionarypatron;

import io.github.cdimascio.dotenv.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DictionaryPatronServiceApplication {

	public DictionaryPatronServiceApplication() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	public static void main(String[] args) {
		new DictionaryPatronServiceApplication();
		SpringApplication.run(DictionaryPatronServiceApplication.class, args);
	}

}
