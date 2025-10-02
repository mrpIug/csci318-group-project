package com.group18.rotionary.domain.agenticai;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * AiResult Entity - Represents the result from an AI service request
 * Part of the Agentic AI bounded context
 */
@Entity
@Table(name = "ai_results")
public class AiResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "request_id")
    private Long requestId;
    
    @Column(name = "result_text", length = 5000)
    private String resultText;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "processing_time_ms")
    private Long processingTimeMs;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "model_used")
    private String modelUsed;
    
    @Column(name = "tokens_used")
    private Integer tokensUsed;
    
    // Default constructor for JPA
    protected AiResult() {}
    
    // Domain constructor
    public AiResult(Long requestId, String resultText, Double confidenceScore, Long processingTimeMs, String modelUsed, Integer tokensUsed) {
        if (requestId == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
        if (resultText == null || resultText.trim().isEmpty()) {
            throw new IllegalArgumentException("Result text cannot be null or empty");
        }
        this.requestId = requestId;
        this.resultText = resultText.trim();
        this.confidenceScore = confidenceScore;
        this.processingTimeMs = processingTimeMs;
        this.modelUsed = modelUsed;
        this.tokensUsed = tokensUsed;
        this.createdAt = LocalDateTime.now();
    }
    
    // Domain methods
    public void updateResult(String newResultText, Double newConfidenceScore) {
        this.resultText = newResultText != null ? newResultText.trim() : this.resultText;
        this.confidenceScore = newConfidenceScore;
    }
    
    // Getters
    public Long getId() { return id; }
    public Long getRequestId() { return requestId; }
    public String getResultText() { return resultText; }
    public Double getConfidenceScore() { return confidenceScore; }
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getModelUsed() { return modelUsed; }
    public Integer getTokensUsed() { return tokensUsed; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiResult aiResult = (AiResult) o;
        return Objects.equals(id, aiResult.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "AiResult{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", resultText='" + resultText + '\'' +
                ", confidenceScore=" + confidenceScore +
                ", processingTimeMs=" + processingTimeMs +
                ", createdAt=" + createdAt +
                ", modelUsed='" + modelUsed + '\'' +
                '}';
    }
}
