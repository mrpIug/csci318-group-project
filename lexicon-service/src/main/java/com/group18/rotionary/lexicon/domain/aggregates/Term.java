package com.group18.rotionary.lexicon.domain.aggregates;

import com.group18.rotionary.lexicon.domain.entities.Definition;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// term aggregate root - main entity for slang words
@Entity
@Table(name = "terms", uniqueConstraints = @UniqueConstraint(columnNames = "word"))
public class Term {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "word", nullable = false, length = 50)
    private String word;
    
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @OneToOne(mappedBy = "term", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JsonManagedReference
    private Definition definition;
    
    @ElementCollection
    @CollectionTable(name = "term_tags", joinColumns = @JoinColumn(name = "term_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    protected Term() {}
    
    public Term(String word, String createdBy) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        this.word = word.trim().toLowerCase();
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDefinition(Definition definition) {
        if (definition != null) {
            definition.setTerm(this);
        }
        this.definition = definition;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty");
        }
        String normalized = tag.toLowerCase().trim();
        if (!this.tags.contains(normalized)) {
            this.tags.add(normalized);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag.toLowerCase().trim());
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    
    public Long getId() { return id; }
    public String getWord() { return word; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public Definition getDefinition() { return definition; }
    public List<String> getTags() { return new ArrayList<>(tags); }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(word, term.word);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
    
    @Override
    public String toString() {
        return "Term{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


