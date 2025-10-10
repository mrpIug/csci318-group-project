package com.group18.rotionary.rotlegame.service;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Map;

/**
 * Rotle Game Service - Handles integration with Lexicon service
 * Part of the Rotle Game bounded context
 */
@Service
public class RotleGameService {
    
    private final RestClient restClient;
    private final String lexiconBaseUrl;
    
    public RotleGameService(@Value("${lexicon.service.url:http://localhost:8081}") String lexiconBaseUrl) {
        this.lexiconBaseUrl = lexiconBaseUrl;
        this.restClient = RestClient.builder()
                .baseUrl(lexiconBaseUrl)
                .build();
    }
    
    /**
     * Fetches a random 5-letter term from the Lexicon service
     */
    public TermInfo startNewGame() {
        try {
            Map<String, Object> response = restClient.get()
                    .uri("/api/terms/random-five")
                    .retrieve()
                    .body(Map.class);
            
            if (response == null) {
                throw new IllegalStateException("No 5-letter terms available in lexicon");
            }
            
            return new TermInfo(
                    Long.valueOf(response.get("id").toString()),
                    response.get("word").toString()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch random 5-letter term from lexicon: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates if a guess is a valid term in the lexicon
     */
    public boolean validateGuess(String guess) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri("/api/terms/exists?word={word}", guess)
                    .retrieve()
                    .body(Map.class);
            
            return response != null && Boolean.TRUE.equals(response.get("exists"));
        } catch (Exception e) {
            // If validation fails, allow the guess (fail gracefully)
            return true;
        }
    }
    
    /**
     * Creates a new game with a random 5-letter term
     */
    public Game createNewGame(String userSession) {
        TermInfo termInfo = startNewGame();
        return new Game(termInfo.word(), termInfo.id(), LocalDate.now(), userSession);
    }
    
    /**
     * Data transfer object for term information
     */
    public record TermInfo(Long id, String word) {}
}
