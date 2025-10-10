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
        return exampleSentencesAiService.generateExamples(term);
    }

    public String etymology(String term) {
        return etymologyAiService.explainEtymology(term);
    }

    public String suggestTags(String word) {
        return tagSuggestionAiService.suggestTags(word);
    }
}
