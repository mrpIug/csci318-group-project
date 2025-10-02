package com.group18.rotionary.domain.events;

/**
 * WordOfTheDayUpdatedEvent - Published when the word of the day is updated
 */
public class WordOfTheDayUpdatedEvent extends DomainEvent {
    
    private final Long termId;
    private final String termWord;
    private final Long queryCount;
    private final String date;
    
    public WordOfTheDayUpdatedEvent(Long termId, String termWord, Long queryCount, String date) {
        super("WordOfTheDayUpdated");
        this.termId = termId;
        this.termWord = termWord;
        this.queryCount = queryCount;
        this.date = date;
    }
    
    public Long getTermId() {
        return termId;
    }
    
    public String getTermWord() {
        return termWord;
    }
    
    public Long getQueryCount() {
        return queryCount;
    }
    
    public String getDate() {
        return date;
    }
    
    @Override
    public String toString() {
        return "WordOfTheDayUpdatedEvent{" +
                "termId=" + termId +
                ", termWord='" + termWord + '\'' +
                ", queryCount=" + queryCount +
                ", date='" + date + '\'' +
                ", " + super.toString() +
                '}';
    }
}
