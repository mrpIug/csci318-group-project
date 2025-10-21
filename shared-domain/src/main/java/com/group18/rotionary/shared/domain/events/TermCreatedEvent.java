package com.group18.rotionary.shared.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// published when a new term is added to the lexicon
public class TermCreatedEvent extends DomainEvent {
    
    private final Long termId;
    private final String termWord;
    private final String createdBy;

    // Default constructor for Jackson deserialisation fallback
    public TermCreatedEvent() {
        super("TermCreated");
        this.termId = 0L;
        this.termWord = "unknown";
        this.createdBy = "unknown";
    }

    @JsonCreator
    public TermCreatedEvent(@JsonProperty(value = "termId", required = false) Long termId,
                           @JsonProperty(value = "termWord", required = false) String termWord,
                           @JsonProperty(value = "createdBy", required = false) String createdBy) {
        super("TermCreated");
        this.termId = termId != null ? termId : 0L;
        this.termWord = termWord != null ? termWord : "unknown";
        this.createdBy = createdBy != null ? createdBy : "unknown";
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
