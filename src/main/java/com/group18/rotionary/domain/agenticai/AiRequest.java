package com.group18.rotionary.domain.agenticai;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * AiRequest Entity - Represents a request to the AI service
 * Part of the Agentic AI bounded context
 */
@Entity
@Table(name = "ai_requests")
public class AiRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "request_type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;
    
    @Column(name = "input_text", length = 2000)
    private String inputText;
    
    @Column(name = "term_id")
    private Long termId;
    
    @Column(name = "term_word")
    private String termWord;
    
    @Column(name = "user_session")
    private String userSession;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
    // Default constructor for JPA
    protected AiRequest() {}
    
    // Domain constructor
    public AiRequest(RequestType requestType, String inputText, Long termId, String termWord, String userSession) {
        if (requestType == null) {
            throw new IllegalArgumentException("Request type cannot be null");
        }
        if (inputText == null || inputText.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty");
        }
        this.requestType = requestType;
        this.inputText = inputText.trim();
        this.termId = termId;
        this.termWord = termWord;
        this.userSession = userSession;
        this.createdAt = LocalDateTime.now();
        this.status = RequestStatus.PENDING;
    }
    
    // Domain methods
    public void markAsProcessing() {
        this.status = RequestStatus.PROCESSING;
    }
    
    public void markAsCompleted() {
        this.status = RequestStatus.COMPLETED;
    }
    
    public void markAsFailed() {
        this.status = RequestStatus.FAILED;
    }
    
    // Getters
    public Long getId() { return id; }
    public RequestType getRequestType() { return requestType; }
    public String getInputText() { return inputText; }
    public Long getTermId() { return termId; }
    public String getTermWord() { return termWord; }
    public String getUserSession() { return userSession; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public RequestStatus getStatus() { return status; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiRequest aiRequest = (AiRequest) o;
        return Objects.equals(id, aiRequest.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "AiRequest{" +
                "id=" + id +
                ", requestType=" + requestType +
                ", inputText='" + inputText + '\'' +
                ", termId=" + termId +
                ", termWord='" + termWord + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
    
    public enum RequestType {
        ETYMOLOGY_ANALYSIS,
        CONTEXT_GENERATION,
        TAG_SUGGESTION,
        EXAMPLE_SENTENCE
    }
    
    public enum RequestStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }
}
