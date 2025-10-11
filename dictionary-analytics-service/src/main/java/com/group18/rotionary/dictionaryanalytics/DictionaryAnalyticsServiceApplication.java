package com.group18.rotionary.dictionaryanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.group18.rotionary.dictionaryanalytics"
})
@EnableScheduling
public class DictionaryAnalyticsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DictionaryAnalyticsServiceApplication.class, args);
	}

}
