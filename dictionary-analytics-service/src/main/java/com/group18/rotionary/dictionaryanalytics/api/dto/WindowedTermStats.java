package com.group18.rotionary.dictionaryanalytics.api.dto;

public class WindowedTermStats {
    private String termWord;
    private Long queryCount;
    private String windowInfo;

    public WindowedTermStats() {}

    public WindowedTermStats(String termWord, Long queryCount, String windowInfo) {
        this.termWord = termWord;
        this.queryCount = queryCount;
        this.windowInfo = windowInfo;
    }

    public String getTermWord() {
        return termWord;
    }

    public void setTermWord(String termWord) {
        this.termWord = termWord;
    }

    public Long getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(Long queryCount) {
        this.queryCount = queryCount;
    }

    public String getWindowInfo() {
        return windowInfo;
    }

    public void setWindowInfo(String windowInfo) {
        this.windowInfo = windowInfo;
    }
}

