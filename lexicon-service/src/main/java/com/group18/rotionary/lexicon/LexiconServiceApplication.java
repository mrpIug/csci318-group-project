package com.group18.rotionary.lexicon;

import io.github.cdimascio.dotenv.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LexiconServiceApplication {

	public LexiconServiceApplication() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	public static void main(String[] args) {
		new LexiconServiceApplication();
		SpringApplication.run(LexiconServiceApplication.class, args);
	}

}
