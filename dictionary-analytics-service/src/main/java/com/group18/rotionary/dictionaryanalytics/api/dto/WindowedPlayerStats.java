package com.group18.rotionary.dictionaryanalytics.api.dto;

public class WindowedPlayerStats {
    private String userSession;
    private Long totalGames;
    private Double winRate;
    private Double averageAttempts;
    private String windowInfo;

    public String getUserSession() { return userSession; }
    public void setUserSession(String userSession) { this.userSession = userSession; }
    
    public Long getTotalGames() { return totalGames; }
    public void setTotalGames(Long totalGames) { this.totalGames = totalGames; }
    
    public Double getWinRate() { return winRate; }
    public void setWinRate(Double winRate) { this.winRate = winRate; }
    
    public Double getAverageAttempts() { return averageAttempts; }
    public void setAverageAttempts(Double averageAttempts) { this.averageAttempts = averageAttempts; }
    
    public String getWindowInfo() { return windowInfo; }
    public void setWindowInfo(String windowInfo) { this.windowInfo = windowInfo; }
}
