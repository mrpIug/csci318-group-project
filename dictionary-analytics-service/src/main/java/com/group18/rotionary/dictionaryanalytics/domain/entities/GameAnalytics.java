package com.group18.rotionary.dictionaryanalytics.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_analytics")
public class GameAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private String targetWord;

    @Column(nullable = false)
    private boolean won;

    @Column(nullable = false)
    private int attemptsCount;

    @Column(nullable = false)
    private String userSession;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected GameAnalytics() {}

    public GameAnalytics(Long gameId, String targetWord, boolean won, int attemptsCount, String userSession) {
        this.gameId = gameId;
        this.targetWord = targetWord;
        this.won = won;
        this.attemptsCount = attemptsCount;
        this.userSession = userSession;
        this.completedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getGameId() { return gameId; }
    public String getTargetWord() { return targetWord; }
    public boolean isWon() { return won; }
    public int getAttemptsCount() { return attemptsCount; }
    public String getUserSession() { return userSession; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
    public void setTargetWord(String targetWord) { this.targetWord = targetWord; }
    public void setWon(boolean won) { this.won = won; }
    public void setAttemptsCount(int attemptsCount) { this.attemptsCount = attemptsCount; }
    public void setUserSession(String userSession) { this.userSession = userSession; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "GameAnalytics{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", targetWord='" + targetWord + '\'' +
                ", won=" + won +
                ", attemptsCount=" + attemptsCount +
                ", userSession='" + userSession + '\'' +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
