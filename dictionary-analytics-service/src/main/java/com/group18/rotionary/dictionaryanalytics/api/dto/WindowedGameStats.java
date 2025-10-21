package com.group18.rotionary.dictionaryanalytics.api.dto;

public class WindowedGameStats {
    private String targetWord;
    private Long gameCount;
    private Double winRate;
    private Double averageAttempts;
    private String windowInfo;

    public WindowedGameStats() {}

    public WindowedGameStats(String targetWord, Long gameCount, Double winRate, Double averageAttempts, String windowInfo) {
        this.targetWord = targetWord;
        this.gameCount = gameCount;
        this.winRate = winRate;
        this.averageAttempts = averageAttempts;
        this.windowInfo = windowInfo;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public Long getGameCount() {
        return gameCount;
    }

    public void setGameCount(Long gameCount) {
        this.gameCount = gameCount;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Double getAverageAttempts() {
        return averageAttempts;
    }

    public void setAverageAttempts(Double averageAttempts) {
        this.averageAttempts = averageAttempts;
    }

    public String getWindowInfo() {
        return windowInfo;
    }

    public void setWindowInfo(String windowInfo) {
        this.windowInfo = windowInfo;
    }
}

