package com.group18.rotionary.agenticai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface ExampleSentencesAiService {
    @SystemMessage("""
        You are an expert in modern slang and internet language.         
        Your task is to generate realistic example sentences showing how slang terms are used in context.
        Guidelines:
        - Create 3 natural example sentences
        - Show different contexts and situations
        - Include both casual and more formal usage when appropriate
        - Make examples relatable and realistic
        - Show proper grammar and punctuation
        - Avoid offensive or inappropriate content
        - Format as numbered list with periods between examples
        - Do not use quotation marks
        - Do not use line breaks, separate examples with periods
        """)
    String generateExamples(@UserMessage String term);
}
