package com.group18.rotionary.dictionarypatron.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "daily_wotd")
public class DailyWOTD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", unique = true, nullable = false)
    private LocalDate date;

    @Column(name = "term_id")
    private Long termId;

    @Column(name = "term_word")
    private String termWord;

    @Column(name = "query_count")
    private Long queryCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected DailyWOTD() {}

    public DailyWOTD(LocalDate date, Long termId, String termWord, Long queryCount) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        if (termId == null) throw new IllegalArgumentException("Term ID cannot be null");
        if (termWord == null || termWord.trim().isEmpty()) throw new IllegalArgumentException("Term word cannot be null or empty");
        this.date = date;
        this.termId = termId;
        this.termWord = termWord;
        this.queryCount = queryCount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTerm(Long newTermId, String newTermWord, Long newQueryCount) {
        this.termId = newTermId;
        this.termWord = newTermWord;
        this.queryCount = newQueryCount;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public Long getTermId() { return termId; }
    public String getTermWord() { return termWord; }
    public Long getQueryCount() { return queryCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWOTD dailyWOTD = (DailyWOTD) o;
        return Objects.equals(date, dailyWOTD.date);
    }

    @Override
    public int hashCode() { return Objects.hash(date); }
}


