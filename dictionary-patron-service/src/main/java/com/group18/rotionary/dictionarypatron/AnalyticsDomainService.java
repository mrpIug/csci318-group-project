package com.group18.rotionary.dictionarypatron;

import com.group18.rotionary.dictionarypatron.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionarypatron.domain.entities.QueryEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Analytics Domain Service - Handles business logic for analytics and trending
 * Part of the Dictionary Patron bounded context
 */
@Service
public class AnalyticsDomainService {
    
    /**
     * Determines the most popular term for a given date based on query events
     */
    public DailyWOTD determineWordOfTheDay(LocalDate date, List<QueryEvent> queryEvents) {
        if (queryEvents == null || queryEvents.isEmpty()) {
            throw new IllegalArgumentException("Query events cannot be null or empty");
        }
        
        // Group query events by term and count occurrences
        var termCounts = queryEvents.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        QueryEvent::getTermId,
                        java.util.stream.Collectors.counting()
                ));
        
        // Find the term with the highest count
        var mostPopularTerm = termCounts.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No query events found"));
        
        // Get the term word from the first query event for this term
        String termWord = queryEvents.stream()
                .filter(event -> event.getTermId().equals(mostPopularTerm.getKey()))
                .findFirst()
                .map(QueryEvent::getTermWord)
                .orElse("Unknown");
        
        return new DailyWOTD(date, mostPopularTerm.getKey(), termWord, mostPopularTerm.getValue());
    }
    
    /**
     * Calculates trending terms based on recent query activity
     */
    public List<Long> getTrendingTerms(List<QueryEvent> recentEvents, int limit) {
        if (recentEvents == null) {
            throw new IllegalArgumentException("Recent events cannot be null");
        }
        
        return recentEvents.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        QueryEvent::getTermId,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(limit)
                .map(java.util.Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Validates if a query event is valid for analytics
     */
    public boolean isValidQueryEvent(QueryEvent queryEvent) {
        return queryEvent != null 
                && queryEvent.getTermId() != null 
                && queryEvent.getTermWord() != null 
                && !queryEvent.getTermWord().trim().isEmpty()
                && queryEvent.getQueryTimestamp() != null;
    }
}
