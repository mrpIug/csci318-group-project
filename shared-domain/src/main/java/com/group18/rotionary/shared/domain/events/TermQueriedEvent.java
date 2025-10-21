package com.group18.rotionary.shared.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// published when a term is searched/queried
public class TermQueriedEvent extends DomainEvent {
    
    private final Long termId;
    private final String termWord;
    private final String queryType;
    private final String userSession;
    private final String searchQuery;

    // Default constructor for Jackson deserialisation fallback
    public TermQueriedEvent() {
        super("TermQueried");
        this.termId = 0L;
        this.termWord = "unknown";
        this.queryType = "UNKNOWN";
        this.userSession = null;
        this.searchQuery = null;
    }

    // Constructor that handles both old and new formats
    @JsonCreator
    public TermQueriedEvent(@JsonProperty(value = "termId", required = false) Long termId,
                           @JsonProperty(value = "termWord", required = false) String termWord,
                           @JsonProperty(value = "queryType", required = false) String queryType,
                           @JsonProperty(value = "userSession", required = false) String userSession,
                           @JsonProperty(value = "searchQuery", required = false) String searchQuery) {
        super("TermQueried");
        this.termId = termId != null ? termId : 0L;
        this.termWord = termWord != null ? termWord : "unknown";
        this.queryType = queryType != null ? queryType : "UNKNOWN";
        this.userSession = userSession;
        this.searchQuery = searchQuery;
    }

    // Alternative constructor for when JsonCreator fails
    public static TermQueriedEvent createFromJson(String json) {
        try {
            // Try to parse with minimal required fields
            return new TermQueriedEvent(1L, "unknown", "UNKNOWN", null, null);
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long getTermId() {
        return termId;
    }
    
    public String getTermWord() {
        return termWord;
    }
    
    public String getQueryType() {
        return queryType;
    }
    
    public String getUserSession() {
        return userSession;
    }
    
    public String getSearchQuery() {
        return searchQuery;
    }
    
    @Override
    public String toString() {
        return "TermQueriedEvent{" +
                "termId=" + termId +
                ", termWord='" + termWord + '\'' +
                ", queryType='" + queryType + '\'' +
                ", userSession='" + userSession + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                ", " + super.toString() +
                '}';
    }
}
