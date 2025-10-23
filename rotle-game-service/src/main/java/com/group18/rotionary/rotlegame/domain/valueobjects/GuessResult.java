package com.group18.rotionary.rotlegame.domain.valueobjects;

import java.util.Objects;

// guess result value object - handles wordle-style letter feedback (correct/present/absent)
public class GuessResult {
    
    public enum LetterFeedback {
        CORRECT('C'),   // guess letter is in word and in the right spot
        PRESENT('P'),   // guess letter is in word but in the wrong spot
        ABSENT('A');    // guess letter is not in word at all
        
        private final char symbol;
        
        LetterFeedback(char symbol) {
            this.symbol = symbol;
        }
        
        public char getSymbol() {
            return symbol;
        }
    }
    
    private final String pattern;
    
    public GuessResult(String guess, String targetWord) {
        if (guess == null || targetWord == null) {
            throw new IllegalArgumentException("Guess and target word cannot be null");
        }
        if (guess.length() != 5 || targetWord.length() != 5) {
            throw new IllegalArgumentException("Both guess and target must be exactly 5 characters");
        }
        
        this.pattern = calculatePattern(guess.toLowerCase(), targetWord.toLowerCase());
    }
    
    private String calculatePattern(String guess, String target) {
        char[] result = new char[5];
        char[] targetChars = target.toCharArray();
        char[] guessChars = guess.toCharArray();
        boolean[] targetUsed = new boolean[5];
        
        // mark correct letters
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == targetChars[i]) {
                result[i] = LetterFeedback.CORRECT.getSymbol();
                targetUsed[i] = true;
            }
        }
        
        // mark present letters
        for (int i = 0; i < 5; i++) {
            if (result[i] == 0) {
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[j] && guessChars[i] == targetChars[j]) {
                        result[i] = LetterFeedback.PRESENT.getSymbol();
                        targetUsed[j] = true;
                        break;
                    }
                }
            }
        }
        
        // mark absent letters
        for (int i = 0; i < 5; i++) {
            if (result[i] == 0) {
                result[i] = LetterFeedback.ABSENT.getSymbol();
            }
        }
        
        return new String(result);
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public LetterFeedback getFeedbackForPosition(int position) {
        if (position < 0 || position >= 5) {
            throw new IllegalArgumentException("Position must be between 0 and 4");
        }
        
        char symbol = pattern.charAt(position);
        for (LetterFeedback feedback : LetterFeedback.values()) {
            if (feedback.getSymbol() == symbol) {
                return feedback;
            }
        }
        throw new IllegalStateException("Invalid pattern symbol: " + symbol);
    }
    
    public boolean isCorrect() {
        return pattern.equals("CCCCC");
    }
    
    public int getCorrectCount() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (getFeedbackForPosition(i) == LetterFeedback.CORRECT) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuessResult that = (GuessResult) o;
        return Objects.equals(pattern, that.pattern);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }
    
    @Override
    public String toString() {
        return pattern;
    }
}
