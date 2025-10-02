package com.group18.rotionary.domain.events;

/**
 * TermCreatedEvent - Published when a new term is added to the lexicon
 */
public class TermCreatedEvent extends DomainEvent {
    
    private final Long termId;
    private final String termWord;
    private final String createdBy;
    
    public TermCreatedEvent(Long termId, String termWord, String createdBy) {
        super("TermCreated");
        this.termId = termId;
        this.termWord = termWord;
        this.createdBy = createdBy;
    }
    
    public Long getTermId() {
        return termId;
    }
    
    public String getTermWord() {
        return termWord;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    @Override
    public String toString() {
        return "TermCreatedEvent{" +
                "termId=" + termId +
                ", termWord='" + termWord + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", " + super.toString() +
                '}';
    }
}
