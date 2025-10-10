package com.group18.rotionary.shared.domain.events;

/**
 * GameCompletedEvent - Published when a Rotle game ends (won or lost)
 */
public class GameCompletedEvent extends DomainEvent {
    
    private final Long gameId;
    private final String targetWord;
    private final boolean won;
    private final int attemptsCount;
    private final String userSession;
    
    public GameCompletedEvent(Long gameId, String targetWord, boolean won, int attemptsCount, String userSession) {
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