package com.group18.rotionary.rotlegame.domain.aggregates;

import com.group18.rotionary.rotlegame.domain.entities.Attempt;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_word", nullable = false)
    private String targetWord;

    @Column(name = "target_word_id")
    private Long targetWordId;

    @Column(name = "game_date", nullable = false)
    private LocalDate gameDate;

    @Column(name = "max_attempts")
    private Integer maxAttempts = 6;

    @Column(name = "current_attempt")
    private Integer currentAttempt = 0;

    @Column(name = "game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Column(name = "user_session")
    private String userSession;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attempt> attempts = new ArrayList<>();

    protected Game() {}

    public Game(String targetWord, Long targetWordId, LocalDate gameDate, String userSession) {
        if (targetWord == null || targetWord.trim().isEmpty()) throw new IllegalArgumentException("Target word cannot be null or empty");
        if (targetWord.length() != 5) throw new IllegalArgumentException("Target word must be exactly 5 characters");
        if (targetWordId == null) throw new IllegalArgumentException("Target word ID cannot be null");
        if (gameDate == null) throw new IllegalArgumentException("Game date cannot be null");
        this.targetWord = targetWord.toLowerCase().trim();
        this.targetWordId = targetWordId;
        this.gameDate = gameDate;
        this.userSession = userSession;
        this.gameStatus = GameStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public Attempt makeAttempt(String guess) {
        if (gameStatus != GameStatus.ACTIVE) throw new IllegalStateException("Game is not active");
        if (currentAttempt >= maxAttempts) throw new IllegalStateException("Maximum attempts reached");
        if (guess == null || guess.trim().isEmpty()) throw new IllegalArgumentException("Guess cannot be null or empty");
        if (guess.length() != 5) throw new IllegalArgumentException("Guess must be exactly 5 characters");

        currentAttempt++;
        Attempt attempt = new Attempt(this, guess.toLowerCase().trim(), currentAttempt);
        attempt.calculateResult(targetWord);
        this.attempts.add(attempt);

        if (guess.toLowerCase().trim().equals(targetWord)) {
            this.gameStatus = GameStatus.WON;
            this.completedAt = LocalDateTime.now();
        } else if (currentAttempt >= maxAttempts) {
            this.gameStatus = GameStatus.LOST;
            this.completedAt = LocalDateTime.now();
        }

        return attempt;
    }

    public boolean isGameOver() { return gameStatus == GameStatus.WON || gameStatus == GameStatus.LOST; }
    public boolean isWon() { return gameStatus == GameStatus.WON; }
    public boolean isLost() { return gameStatus == GameStatus.LOST; }
    public int getRemainingAttempts() { return maxAttempts - currentAttempt; }

    public Long getId() { return id; }
    public String getTargetWord() { return targetWord; }
    public Long getTargetWordId() { return targetWordId; }
    public LocalDate getGameDate() { return gameDate; }
    public Integer getMaxAttempts() { return maxAttempts; }
    public Integer getCurrentAttempt() { return currentAttempt; }
    public GameStatus getGameStatus() { return gameStatus; }
    public String getUserSession() { return userSession; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public List<Attempt> getAttempts() { return new ArrayList<>(attempts); }

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Game game = (Game) o; return Objects.equals(id, game.id); }
    @Override
    public int hashCode() { return Objects.hash(id); }

    public enum GameStatus { ACTIVE, WON, LOST }
}


