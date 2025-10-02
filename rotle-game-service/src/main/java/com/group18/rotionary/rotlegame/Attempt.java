package com.group18.rotionary.domain.rotlegame;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Attempt Entity - Represents a single guess attempt in a Rotle game
 * Part of the Rotle Game bounded context aggregate
 */
@Entity
@Table(name = "attempts")
public class Attempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "guess", nullable = false)
    private String guess;
    
    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;
    
    @Column(name = "result", length = 5)
    private String result; // "G" for green (correct), "Y" for yellow (wrong position), "B" for black (not in word)
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    // Default constructor for JPA
    protected Attempt() {}
    
    // Domain constructor
    public Attempt(Game game, String guess, Integer attemptNumber) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        if (guess == null || guess.trim().isEmpty()) {
            throw new IllegalArgumentException("Guess cannot be null or empty");
        }
        if (attemptNumber == null || attemptNumber <= 0) {
            throw new IllegalArgumentException("Attempt number must be positive");
        }
        this.game = game;
        this.guess = guess.toLowerCase().trim();
        this.attemptNumber = attemptNumber;
        this.createdAt = LocalDateTime.now();
        this.isCorrect = false;
        this.result = "";
    }
    
    // Domain methods
    public void calculateResult(String targetWord) {
        if (targetWord == null || targetWord.length() != 5) {
            throw new IllegalArgumentException("Target word must be 5 characters");
        }
        
        char[] target = targetWord.toLowerCase().toCharArray();
        char[] guessChars = this.guess.toCharArray();
        char[] result = new char[5];
        
        // First pass: mark exact matches (green)
        boolean[] targetUsed = new boolean[5];
        boolean[] guessUsed = new boolean[5];
        
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == target[i]) {
                result[i] = 'G'; // Green - correct letter in correct position
                targetUsed[i] = true;
                guessUsed[i] = true;
            }
        }
        
        // Second pass: mark wrong position matches (yellow)
        for (int i = 0; i < 5; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[j] && guessChars[i] == target[j]) {
                        result[i] = 'Y'; // Yellow - correct letter in wrong position
                        targetUsed[j] = true;
                        guessUsed[i] = true;
                        break;
                    }
                }
            }
        }
        
        // Third pass: mark not in word (black)
        for (int i = 0; i < 5; i++) {
            if (!guessUsed[i]) {
                result[i] = 'B'; // Black - letter not in word
            }
        }
        
        this.result = new String(result);
        this.isCorrect = this.guess.equals(targetWord.toLowerCase());
    }
    
    public boolean isCorrectGuess() {
        return Boolean.TRUE.equals(isCorrect);
    }
    
    public boolean hasGreenLetters() {
        return result != null && result.contains("G");
    }
    
    public boolean hasYellowLetters() {
        return result != null && result.contains("Y");
    }
    
    // Getters
    public Long getId() { return id; }
    public String getGuess() { return guess; }
    public Integer getAttemptNumber() { return attemptNumber; }
    public String getResult() { return result; }
    public Boolean getIsCorrect() { return isCorrect; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Game getGame() { return game; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attempt attempt = (Attempt) o;
        return Objects.equals(id, attempt.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Attempt{" +
                "id=" + id +
                ", guess='" + guess + '\'' +
                ", attemptNumber=" + attemptNumber +
                ", result='" + result + '\'' +
                ", isCorrect=" + isCorrect +
                ", createdAt=" + createdAt +
                '}';
    }
}
