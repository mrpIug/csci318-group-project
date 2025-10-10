package com.group18.rotionary.agenticai.infrastructure;

import dev.langchain4j.model.chat.listener.ChatModelListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {
    @Bean
    ChatModelListener chatModelLogger() {
        return new ModelLogger();
    }
}
