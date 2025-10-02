package com.group18.rotionary.shared.domain.events;

/**
 * GameCompletedEvent - Published when a Rotle game is completed
 */
public class GameCompletedEvent extends DomainEvent {
    
    private final Long gameId;
    private final String targetWord;
    private final String gameStatus;
    private final Integer attemptsUsed;
    private final String userSession;
    
    public GameCompletedEvent(Long gameId, String targetWord, String gameStatus, Integer attemptsUsed, String userSession) {
        super("GameCompleted");
        this.gameId = gameId;
        this.targetWord = targetWord;
        this.gameStatus = gameStatus;
        this.attemptsUsed = attemptsUsed;
        this.userSession = userSession;
    }
    
    public Long getGameId() {
        return gameId;
    }
    
    public String getTargetWord() {
        return targetWord;
    }
    
    public String getGameStatus() {
        return gameStatus;
    }
    
    public Integer getAttemptsUsed() {
        return attemptsUsed;
    }
    
    public String getUserSession() {
        return userSession;
    }
    
    @Override
    public String toString() {
        return "GameCompletedEvent{" +
                "gameId=" + gameId +
                ", targetWord='" + targetWord + '\'' +
                ", gameStatus='" + gameStatus + '\'' +
                ", attemptsUsed=" + attemptsUsed +
                ", userSession='" + userSession + '\'' +
                ", " + super.toString() +
                '}';
    }
}
