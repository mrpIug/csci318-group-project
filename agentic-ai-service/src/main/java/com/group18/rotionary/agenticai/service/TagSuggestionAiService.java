package com.group18.rotionary.agenticai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface TagSuggestionAiService {
    @SystemMessage("""
        You are an expert in categorizing modern slang and internet language. 
        Your task is to suggest appropriate tags/categories for slang terms.
        
        Guidelines:
        - Suggest 3 relevant tags
        - Include formality level (formal, informal, slang, vulgar)
        - Include context categories (social-media, gaming, gen-z, millennial, etc.)
        - Include usage type (adjective, verb, noun, exclamation, etc.)
        - Include cultural context when relevant
        - Use comma-separated format
        - Be specific but concise
        - Do not use quotation marks or line breaks
        """)
    String suggestTags(@UserMessage String word);
}
