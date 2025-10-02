package com.group18.rotionary.domain.lexicon;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Term Entity - Core entity representing a slang word in the lexicon
 * This is the aggregate root for the Lexicon bounded context
 */
@Entity
@Table(name = "terms")
public class Term {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String word;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Definition> definitions = new ArrayList<>();
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "term_tags",
        joinColumns = @JoinColumn(name = "term_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();
    
    // Default constructor for JPA
    protected Term() {}
    
    // Domain constructor
    public Term(String word, String description, String createdBy) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        this.word = word.toLowerCase().trim();
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Domain methods
    public void addDefinition(Definition definition) {
        if (definition == null) {
            throw new IllegalArgumentException("Definition cannot be null");
        }
        definition.setTerm(this);
        this.definitions.add(definition);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeDefinition(Definition definition) {
        if (definition != null) {
            this.definitions.remove(definition);
            definition.setTerm(null);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeTag(Tag tag) {
        if (tag != null) {
            this.tags.remove(tag);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    public Long getId() { return id; }
    public String getWord() { return word; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public List<Definition> getDefinitions() { return new ArrayList<>(definitions); }
    public List<Tag> getTags() { return new ArrayList<>(tags); }
    
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
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
