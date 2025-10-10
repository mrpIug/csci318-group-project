package com.group18.rotionary.agenticai.service;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiUseCases {

    private final GoogleAiGeminiChatModel model;

    public AiUseCases(
            @Value("${langchain4j.google-ai-gemini.chat-model.api-key}") String apiKey,
            @Value("${langchain4j.google-ai-gemini.chat-model.model-name}") String modelName,
            @Value("${langchain4j.google-ai-gemini.chat-model.temperature:0.7}") double temperature,
            @Value("${langchain4j.google-ai-gemini.chat-model.timeout:PT60s}") String timeout
    ) {
        this.model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .build();
    }

    public String exampleSentences(String term) {
        String system = "You are an expert in modern slang and internet language. Generate 3 realistic example sentences as a numbered list separated by periods, no quotes, no line breaks.";
        String user = "Generate 3 concise example sentences that correctly use the slang term: " + term + ".";
        return model.generate(system + "\n" + user);
    }

    public String etymology(String term) {
        String system = "You are an expert linguist specializing in modern slang and internet language. Explain etymology succinctly in one paragraph with sentences separated by periods, no quotes, no line breaks.";
        String user = "Explain the etymology and timeline for the slang term: " + term + ".";
        return model.generate(system + "\n" + user);
    }

    public String suggestTags(String word, String description) {
        String system = "You categorize modern slang. Suggest 3-6 concise tags, comma-separated, no quotes, no line breaks.";
        String user = "Suggest tags for the slang term '" + word + "' described as '" + description + "'.";
        return model.generate(system + "\n" + user);
    }
}
