package com.group18.rotionary.shared.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// published when a rotle game ends (won or lost)
public class GameCompletedEvent extends DomainEvent {
    
    private final Long gameId;
    private final String targetWord;
    private final boolean won;
    private final int attemptsCount;
    private final String userSession;
    
    // Default constructor for Jackson deserialisation fallback
    public GameCompletedEvent() {
        super("GameCompleted");
        this.gameId = 0L;
        this.targetWord = "unknown";
        this.won = false;
        this.attemptsCount = 0;
        this.userSession = null;
    }

    // Constructor that handles old and new rotle game completed event formats
    @JsonCreator
    public GameCompletedEvent(@JsonProperty(value = "gameId", required = false) Long gameId,
                             @JsonProperty(value = "targetWord", required = false) String targetWord,
                             @JsonProperty(value = "won", required = false) boolean won,
                             @JsonProperty(value = "attemptsCount", required = false) int attemptsCount,
                             @JsonProperty(value = "userSession", required = false) String userSession) {
        super("GameCompleted");
        this.gameId = gameId != null ? gameId : 0L;
        this.targetWord = targetWord != null ? targetWord : "unknown";
        this.won = won;
        this.attemptsCount = attemptsCount;
        this.userSession = userSession;
    }
    
    public Long getGameId() {
        return gameId;
    }
    
    public String getTargetWord() {
        return targetWord;
    }
    
    public boolean isWon() {
        return won;
    }
    
    public int getAttemptsCount() {
        return attemptsCount;
    }
    
    public String getUserSession() {
        return userSession;
    }
    
    @Override
    public String toString() {
        return "GameCompletedEvent{" +
                "gameId=" + gameId +
                ", targetWord='" + targetWord + '\'' +
                ", won=" + won +
                ", attemptsCount=" + attemptsCount +
                ", userSession='" + userSession + '\'' +
                ", " + super.toString() +
                '}';
    }
}