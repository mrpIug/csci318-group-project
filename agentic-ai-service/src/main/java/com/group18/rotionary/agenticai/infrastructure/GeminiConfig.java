package com.group18.rotionary.agenticai.infrastructure;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

@Configuration
public class GeminiConfig {

    @Bean
    public ChatLanguageModel geminiChatModel(
            @Value("${langchain4j.google-ai-gemini.chat-model.api-key}") String apiKey,
            @Value("${langchain4j.google-ai-gemini.chat-model.model-name}") String modelName,
            @Value("${langchain4j.google-ai-gemini.chat-model.temperature:0.7}") double temperature
    ) {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .build();
    }
}


