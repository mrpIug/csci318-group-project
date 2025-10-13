package com.group18.rotionary.dictionaryanalytics;

import com.group18.rotionary.dictionaryanalytics.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionaryanalytics.domain.entities.QueryEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// handles business logic for analytics and trending
@Service
public class AnalyticsDomainService {
    
    // determines the most popular term for a given date based on query events
    public DailyWOTD determineWordOfTheDay(LocalDate date, List<QueryEvent> queryEvents) {
        if (queryEvents == null || queryEvents.isEmpty()) {
            throw new IllegalArgumentException("Query events cannot be null or empty");
        }
        
        // group query events by term and count occurrences
        var termCounts = queryEvents.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        QueryEvent::getTermId,
                        java.util.stream.Collectors.counting()
                ));
        
        // find the term with the highest count
        var mostPopularTerm = termCounts.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No query events found"));
        
        // get the term word from the first query event for this term
        String termWord = queryEvents.stream()
                .filter(event -> event.getTermId().equals(mostPopularTerm.getKey()))
                .findFirst()
                .map(QueryEvent::getTermWord)
                .orElse("Unknown");
        
        return new DailyWOTD(date, mostPopularTerm.getKey(), termWord, mostPopularTerm.getValue());
    }
    
    // calculates trending terms based on recent query activity
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
    
    // validates if a query event is valid for analytics
    public boolean isValidQueryEvent(QueryEvent queryEvent) {
        return queryEvent != null 
                && queryEvent.getTermId() != null 
                && queryEvent.getTermWord() != null 
                && !queryEvent.getTermWord().trim().isEmpty()
                && queryEvent.getQueryTimestamp() != null;
    }
}
