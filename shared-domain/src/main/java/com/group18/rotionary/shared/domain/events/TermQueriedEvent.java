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
    
    @JsonCreator
    public TermQueriedEvent(@JsonProperty("termId") Long termId, 
                           @JsonProperty("termWord") String termWord, 
                           @JsonProperty("queryType") String queryType, 
                           @JsonProperty("userSession") String userSession, 
                           @JsonProperty("searchQuery") String searchQuery) {
        super("TermQueried");
        this.termId = termId;
        this.termWord = termWord;
        this.queryType = queryType;
        this.userSession = userSession;
        this.searchQuery = searchQuery;
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
