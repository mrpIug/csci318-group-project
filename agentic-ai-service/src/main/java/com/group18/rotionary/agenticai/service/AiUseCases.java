package com.group18.rotionary.agenticai.service;

import org.springframework.stereotype.Service;

@Service
public class AiUseCases {

    private final EtymologyAiService etymologyAiService;
    private final ExampleSentencesAiService exampleSentencesAiService;
    private final TagSuggestionAiService tagSuggestionAiService;

    public AiUseCases(EtymologyAiService etymologyAiService, 
                     ExampleSentencesAiService exampleSentencesAiService,
                     TagSuggestionAiService tagSuggestionAiService) {
        this.etymologyAiService = etymologyAiService;
        this.exampleSentencesAiService = exampleSentencesAiService;
        this.tagSuggestionAiService = tagSuggestionAiService;
    }

    public String exampleSentences(String term) {
        return sanitizeResponse(exampleSentencesAiService.generateExamples(term));
    }

    public String etymology(String term) {
        return sanitizeResponse(etymologyAiService.explainEtymology(term));
    }

    public String suggestTags(String word) {
        return sanitizeResponse(tagSuggestionAiService.suggestTags(word));
    }

    private String sanitizeResponse(String response) {
        if (response == null) {
            return "";
        }
        
        // Clean up escaped characters and newlines
        return response
            .replaceAll("\\\\n", " ")           // Replace \n with space
            .replaceAll("\\\\\"", "\"")         // Replace \" with "
            .replaceAll("\\\\'", "'")          // Replace \' with '
            .replaceAll("\\n", " ")            // Replace actual newlines with space
            .replaceAll("\\r", " ")            // Replace carriage returns with space
            .replaceAll("\\s+", " ")           // Replace multiple whitespace with single space
            .trim();                           // Remove leading/trailing whitespace
    }
}
