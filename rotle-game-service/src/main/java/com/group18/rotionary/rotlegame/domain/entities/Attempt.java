package com.group18.rotionary.rotlegame.domain.entities;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private String result;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    protected Attempt() {}

    public Attempt(Game game, String guess, Integer attemptNumber) {
        if (game == null) throw new IllegalArgumentException("Game cannot be null");
        if (guess == null || guess.trim().isEmpty()) throw new IllegalArgumentException("Guess cannot be null or empty");
        if (attemptNumber == null || attemptNumber <= 0) throw new IllegalArgumentException("Attempt number must be positive");
        this.game = game;
        this.guess = guess.toLowerCase().trim();
        this.attemptNumber = attemptNumber;
        this.createdAt = LocalDateTime.now();
        this.isCorrect = false;
        this.result = "";
    }

    public void calculateResult(String targetWord) {
        if (targetWord == null || targetWord.length() != 5) throw new IllegalArgumentException("Target word must be 5 characters");

        char[] target = targetWord.toLowerCase().toCharArray();
        char[] guessChars = this.guess.toCharArray();
        char[] result = new char[5];

        boolean[] targetUsed = new boolean[5];
        boolean[] guessUsed = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == target[i]) {
                result[i] = 'G';
                targetUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[j] && guessChars[i] == target[j]) {
                        result[i] = 'Y';
                        targetUsed[j] = true;
                        guessUsed[i] = true;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!guessUsed[i]) {
                result[i] = 'B';
            }
        }

        this.result = new String(result);
        this.isCorrect = this.guess.equals(targetWord.toLowerCase());
    }

    public Long getId() { return id; }
    public String getGuess() { return guess; }
    public Integer getAttemptNumber() { return attemptNumber; }
    public String getResult() { return result; }
    public Boolean getIsCorrect() { return isCorrect; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Game getGame() { return game; }

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Attempt attempt = (Attempt) o; return Objects.equals(id, attempt.id); }
    @Override
    public int hashCode() { return Objects.hash(id); }
}


