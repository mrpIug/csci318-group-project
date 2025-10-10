package com.group18.rotionary.agenticai.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Etymology Value Object - Immutable representation of word etymology information
 * Encapsulates structured etymology data from AI analysis
 */
public class Etymology {
    
    private final String origin;
    private final String evolutionTimeline;
    private final String notableUsage;
    private final String fullText;
    
    @JsonCreator
    public Etymology(
            @JsonProperty("origin") String origin,
            @JsonProperty("evolutionTimeline") String evolutionTimeline,
            @JsonProperty("notableUsage") String notableUsage,
            @JsonProperty("fullText") String fullText) {
        this.origin = origin;
        this.evolutionTimeline = evolutionTimeline;
        this.notableUsage = notableUsage;
        this.fullText = fullText;
    }
    
    public Etymology(String fullText) {
        this(null, null, null, fullText);
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public String getEvolutionTimeline() {
        return evolutionTimeline;
    }
    
    public String getNotableUsage() {
        return notableUsage;
    }
    
    public String getFullText() {
        return fullText;
    }
    
    public boolean isStructured() {
        return origin != null && evolutionTimeline != null && notableUsage != null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etymology etymology = (Etymology) o;
        return Objects.equals(origin, etymology.origin) &&
               Objects.equals(evolutionTimeline, etymology.evolutionTimeline) &&
               Objects.equals(notableUsage, etymology.notableUsage) &&
               Objects.equals(fullText, etymology.fullText);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(origin, evolutionTimeline, notableUsage, fullText);
    }
    
    @Override
    public String toString() {
        return fullText != null ? fullText : "Etymology{origin='" + origin + "', timeline='" + evolutionTimeline + "'}";
    }
}
