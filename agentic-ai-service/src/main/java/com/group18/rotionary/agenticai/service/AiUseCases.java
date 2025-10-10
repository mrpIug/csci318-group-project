package com.group18.rotionary.agenticai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiUseCases {

    public AiUseCases(@Value("${langchain4j.google-ai-gemini.chat-model.api-key}") String apiKey) {
        // Initialize AI model here if needed
        // For now, we'll use mock responses
    }

    public String exampleSentences(String term) {
        return "1. I'm going to yeet this ball across the field. 2. Did you see him yeet that phone? 3. She yeeted herself into the pool.";
    }

    public String etymology(String term) {
        return "The term '" + term + "' originated in internet culture and gaming communities. It gained popularity through social media platforms and streaming services, becoming widely adopted by Generation Z. The word evolved from its original context to represent various forms of energetic action or expression.";
    }

    public String suggestTags(String word) {
        return "slang, gen-z, informal, internet";
    }
}
