package com.group18.rotionary.dictionaryanalytics.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    protected QueryEvent() {}

    public QueryEvent(Long termId, String termWord, QueryType queryType, String userSession, String searchQuery) {
        this.termId = termId;
        this.termWord = termWord;
        this.queryType = queryType;
        this.userSession = userSession;
        this.searchQuery = searchQuery;
        this.queryTimestamp = LocalDateTime.now();
    }

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
    public int hashCode() { return Objects.hash(id); }

    public enum QueryType {
        BY_WORD, BY_TAG, BY_ID, WORD_OF_THE_DAY, RANDOM_WORD, ROTLE_GAME
    }
}


