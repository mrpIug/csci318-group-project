package com.group18.rotionary.lexicon;

import com.group18.rotionary.domain.lexicon.Term;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Lexicon Domain Service - Handles business logic for lexicon operations
 * Part of the Lexicon bounded context
 */
@Service
public class LexiconDomainService {
    
    private final Random random = new Random();
    
    /**
     * Validates if a term meets the requirements for the lexicon
     */
    public boolean isValidTerm(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        
        String cleanWord = word.trim().toLowerCase();
        
        // Basic validation rules
        return cleanWord.length() >= 2 
                && cleanWord.length() <= 50 
                && cleanWord.matches("^[a-zA-Z]+$"); // Only letters
    }
    
    /**
     * Validates if a definition meets the requirements
     */
    public boolean isValidDefinition(String meaning) {
        if (meaning == null || meaning.trim().isEmpty()) {
            return false;
        }
        
        String cleanMeaning = meaning.trim();
        
        // Basic validation rules
        return cleanMeaning.length() >= 10 
                && cleanMeaning.length() <= 2000;
    }
    
    /**
     * Selects a random term from the provided list
     */
    public Term selectRandomTerm(List<Term> terms) {
        if (terms == null || terms.isEmpty()) {
            throw new IllegalArgumentException("Terms list cannot be null or empty");
        }
        
        int randomIndex = random.nextInt(terms.size());
        return terms.get(randomIndex);
    }
    
    /**
     * Selects a random 5-character term for Rotle game
     */
    public Term selectRandomTermForRotle(List<Term> terms) {
        if (terms == null || terms.isEmpty()) {
            throw new IllegalArgumentException("Terms list cannot be null or empty");
        }
        
        // Filter terms that are exactly 5 characters
        List<Term> fiveCharTerms = terms.stream()
                .filter(term -> term.getWord().length() == 5)
                .collect(java.util.stream.Collectors.toList());
        
        if (fiveCharTerms.isEmpty()) {
            throw new IllegalStateException("No 5-character terms available for Rotle game");
        }
        
        int randomIndex = random.nextInt(fiveCharTerms.size());
        return fiveCharTerms.get(randomIndex);
    }
    
    /**
     * Suggests tags for a term based on its content
     */
    public List<String> suggestTagsForTerm(String word, String description) {
        // This is a simplified implementation
        // In a real application, this might use AI or more sophisticated logic
        List<String> suggestions = new java.util.ArrayList<>();
        
        if (word != null && description != null) {
            String content = (word + " " + description).toLowerCase();
            
            if (content.contains("gaming") || content.contains("game")) {
                suggestions.add("gaming");
            }
            if (content.contains("social") || content.contains("media")) {
                suggestions.add("social-media");
            }
            if (content.contains("internet") || content.contains("online")) {
                suggestions.add("internet");
            }
            if (content.contains("young") || content.contains("teen")) {
                suggestions.add("youth");
            }
            if (content.contains("slang") || content.contains("informal")) {
                suggestions.add("slang");
            }
        }
        
        return suggestions;
    }
    
    /**
     * Normalizes a word for consistent storage and comparison
     */
    public String normalizeWord(String word) {
        if (word == null) {
            return null;
        }
        
        return word.trim().toLowerCase();
    }
}
