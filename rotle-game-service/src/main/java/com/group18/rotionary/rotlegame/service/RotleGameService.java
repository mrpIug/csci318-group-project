package com.group18.rotionary.rotlegame.service;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Map;

// handles integration with lexicon service for rotle game
@Service
public class RotleGameService {
    
    private final RestClient restClient;
    
    public RotleGameService(@Value("${lexicon.service.url:http://localhost:8081}") String lexiconBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(lexiconBaseUrl)
                .build();
    }
    
    // grabs a random 5-letter term from lexicon service
    @SuppressWarnings("unchecked")
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
    
    // validates if guess term exists in lexicon
    @SuppressWarnings("unchecked")
    public boolean validateGuess(String guess) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri("/api/terms/exists?word={word}", guess)
                    .retrieve()
                    .body(Map.class);
            
            return response != null && Boolean.TRUE.equals(response.get("exists"));
        } catch (Exception e) {
            // allow the guess if validation fails
            return true;
        }
    }
    
    // creates a new game with random 5-letter term
    public Game createNewGame(String userSession) {
        TermInfo termInfo = startNewGame();
        return new Game(termInfo.word(), termInfo.id(), LocalDate.now(), userSession);
    }
    
    // data transfer object for term information
    public record TermInfo(Long id, String word) {}
}
