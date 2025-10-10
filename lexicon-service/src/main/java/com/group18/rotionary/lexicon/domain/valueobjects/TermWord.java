package com.group18.rotionary.lexicon.domain.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * TermWord Value Object - Immutable representation of a slang term word
 * Enforces validation rules and ensures consistent formatting
 */
@Embeddable
public class TermWord {
    
    private String value;
    
    protected TermWord() {} // JPA requirement
    
    public TermWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        
        String cleanWord = word.trim().toLowerCase();
        
        // Validation rules from LexiconDomainService
        if (cleanWord.length() < 2 || cleanWord.length() > 50) {
            throw new IllegalArgumentException("Word must be between 2 and 50 characters");
        }
        
        if (!cleanWord.matches("^[a-zA-Z]+$")) {
            throw new IllegalArgumentException("Word must contain only letters");
        }
        
        this.value = cleanWord;
    }
    
    public String getValue() {
        return value;
    }
    
    public int length() {
        return value.length();
    }
    
    public boolean isFiveLetters() {
        return value.length() == 5;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermWord termWord = (TermWord) o;
        return Objects.equals(value, termWord.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
