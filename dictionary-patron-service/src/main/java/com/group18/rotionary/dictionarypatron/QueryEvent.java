package com.group18.rotionary.domain.dictionarypatron;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * QueryEvent Entity - Represents a search query event for analytics
 * Part of the Dictionary Patron bounded context
 */
@Entity
@Table(name = "query_events")
public class QueryEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "term_id")
    private Long termId;
    
    @Column(name = "term_word")
    private String termWord;
    
    @Column(name = "query_type")
    @Enumerated(EnumType.STRING)
    private QueryType queryType;
    
    @Column(name = "query_timestamp")
    private LocalDateTime queryTimestamp;
    
    @Column(name = "user_session")
    private String userSession;
    
    @Column(name = "search_query")
    private String searchQuery;
    
    // Default constructor for JPA
    protected QueryEvent() {}
    
    // Domain constructor
    public QueryEvent(Long termId, String termWord, QueryType queryType, String userSession, String searchQuery) {
        this.termId = termId;
        this.termWord = termWord;
        this.queryType = queryType;
        this.userSession = userSession;
        this.searchQuery = searchQuery;
        this.queryTimestamp = LocalDateTime.now();
    }
    
    // Getters
    public Long getId() { return id; }
    public Long getTermId() { return termId; }
    public String getTermWord() { return termWord; }
    public QueryType getQueryType() { return queryType; }
    public LocalDateTime getQueryTimestamp() { return queryTimestamp; }
    public String getUserSession() { return userSession; }
    public String getSearchQuery() { return searchQuery; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryEvent that = (QueryEvent) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "QueryEvent{" +
                "id=" + id +
                ", termId=" + termId +
                ", termWord='" + termWord + '\'' +
                ", queryType=" + queryType +
                ", queryTimestamp=" + queryTimestamp +
                '}';
    }
    
    public enum QueryType {
        BY_WORD,
        BY_TAG,
        BY_ID,
        WORD_OF_THE_DAY,
        RANDOM_WORD,
        ROTLE_GAME
    }
}
