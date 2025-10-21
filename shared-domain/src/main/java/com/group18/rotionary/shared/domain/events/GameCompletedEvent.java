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
    
    @JsonCreator
    public GameCompletedEvent(@JsonProperty("gameId") Long gameId, 
                             @JsonProperty("targetWord") String targetWord, 
                             @JsonProperty("won") boolean won, 
                             @JsonProperty("attemptsCount") int attemptsCount, 
                             @JsonProperty("userSession") String userSession) {
        super("GameCompleted");
        this.gameId = gameId;
        this.targetWord = targetWord;
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