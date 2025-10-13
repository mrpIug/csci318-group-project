package com.group18.rotionary.lexicon;
import com.group18.rotionary.lexicon.domain.aggregates.Term;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

// handles business logic for lexicon operations
@Service
public class LexiconDomainService {
    
    private final Random random = new Random();
    
    // validate term meets requirements (2-50 chars, letters only)
    public boolean isValidTerm(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        
        String cleanWord = word.trim().toLowerCase();
        
        return cleanWord.length() >= 2 
                && cleanWord.length() <= 50 
                && cleanWord.matches("^[a-zA-Z]+$");
    }
    
    // validate definition (10-2000 chars)
    public boolean isValidDefinition(String meaning) {
        if (meaning == null || meaning.trim().isEmpty()) {
            return false;
        }
        
        String cleanMeaning = meaning.trim();
        
        return cleanMeaning.length() >= 10 
                && cleanMeaning.length() <= 2000;
    }
    
    // picks a random term from the list
    public Term selectRandomTerm(List<Term> terms) {
        if (terms == null || terms.isEmpty()) {
            throw new IllegalArgumentException("Terms list cannot be null or empty");
        }
        
        int randomIndex = random.nextInt(terms.size());
        return terms.get(randomIndex);
    }
    
    // picks a random 5-letter term for rotle game
    public Term selectRandomTermForRotle(List<Term> terms) {
        if (terms == null || terms.isEmpty()) {
            throw new IllegalArgumentException("Terms list cannot be null or empty");
        }
        
        List<Term> fiveCharTerms = terms.stream()
                .filter(term -> term.getWord().length() == 5)
                .collect(java.util.stream.Collectors.toList());
        
        if (fiveCharTerms.isEmpty()) {
            throw new IllegalStateException("No 5-character terms available for Rotle game");
        }
        
        int randomIndex = random.nextInt(fiveCharTerms.size());
        return fiveCharTerms.get(randomIndex);
    }
    
    // normalizes word for storage/comparison
    public String normalizeWord(String word) {
        if (word == null) {
            return null;
        }
        
        return word.trim().toLowerCase();
    }
}
