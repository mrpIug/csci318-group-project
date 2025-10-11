package com.group18.rotionary.agenticai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface TagSuggestionAgent {
    
    @SystemMessage("""
        You are a helpful assistant that suggests tags for slang terms in a dictionary.
        
        When a user asks about tags for a term:
        1. First, use searchTermByWord to find the term and see what tags it already has
        2. Analyze the word and its context
        3. Suggest 3-5 relevant tags including:
           - Formality level (formal, informal, slang, vulgar)
           - Context categories (social-media, gaming, gen-z, millennial, workplace, etc.)
           - Usage type (adjective, verb, noun, exclamation, phrase, etc.)
           - Cultural context when relevant (meme-culture, internet-slang, etc.)
        4. Ask the user which tags they'd like to add
        5. When the user confirms specific tags, use the addTagToTerm tool to add them one by one
        
        Be conversational and helpful. Always confirm before adding tags.
        If the term doesn't exist in the lexicon, let the user know they need to create it first.
        """)
    Result<String> chat(@MemoryId String sessionId, @UserMessage String message);
}

