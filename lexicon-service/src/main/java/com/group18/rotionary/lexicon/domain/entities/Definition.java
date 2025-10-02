package com.group18.rotionary.lexicon.domain.entities;

import com.group18.rotionary.lexicon.domain.aggregates.Term;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "definitions")
public class Definition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 2000)
    private String meaning;
    
    @Column(length = 1000)
    private String example;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;
    
    protected Definition() {}
    
    public Definition(String meaning, String example, String createdBy) {
        if (meaning == null || meaning.trim().isEmpty()) {
            throw new IllegalArgumentException("Meaning cannot be null or empty");
        }
        this.meaning = meaning.trim();
        this.example = example;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateMeaning(String newMeaning) {
        if (newMeaning == null || newMeaning.trim().isEmpty()) {
            throw new IllegalArgumentException("Meaning cannot be null or empty");
        }
        this.meaning = newMeaning.trim();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateExample(String newExample) {
        this.example = newExample;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public String getMeaning() { return meaning; }
    public String getExample() { return example; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public Term getTerm() { return term; }
    
    public void setTerm(Term term) {
        this.term = term;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Definition that = (Definition) o;
        return Objects.equals(meaning, that.meaning) && Objects.equals(term, that.term);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(meaning, term);
    }
    
    @Override
    public String toString() {
        return "Definition{" +
                "id=" + id +
                ", meaning='" + meaning + '\'' +
                ", example='" + example + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


