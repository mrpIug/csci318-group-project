package com.group18.rotionary.dictionarypatron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.group18.rotionary.dictionarypatron"
})
public class DictionaryPatronServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DictionaryPatronServiceApplication.class, args);
	}

}
