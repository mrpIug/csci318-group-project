package com.group18.rotionary.dictionaryanalytics.infrastructure.stream;

import com.group18.rotionary.dictionaryanalytics.domain.entities.GameAnalytics;
import com.group18.rotionary.dictionaryanalytics.repository.GameAnalyticsRepository;
import com.group18.rotionary.shared.domain.events.GameCompletedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class GameAnalyticsConsumer {

    private final GameAnalyticsRepository gameAnalyticsRepository;

    public GameAnalyticsConsumer(GameAnalyticsRepository gameAnalyticsRepository) {
        this.gameAnalyticsRepository = gameAnalyticsRepository;
    }

    @Bean
    public Consumer<GameCompletedEvent> consumeGameCompleted() {
        return event -> {
            System.out.println("Received GameCompletedEvent: Game ID " + event.getGameId() + 
                             ", Target: " + event.getTargetWord() + 
                             ", Won: " + event.isWon() + 
                             ", Attempts: " + event.getAttemptsCount());
            
            try {
                // Check if this game has already been processed
                if (gameAnalyticsRepository.findByGameId(event.getGameId()).isPresent()) {
                    System.out.println("Game " + event.getGameId() + " already processed, skipping duplicate");
                    return;
                }

                // Create new game analytics record
                GameAnalytics analytics = new GameAnalytics(
                    event.getGameId(),
                    event.getTargetWord(),
                    event.isWon(),
                    event.getAttemptsCount(),
                    event.getUserSession()
                );

                gameAnalyticsRepository.save(analytics);
                
                System.out.println("Successfully saved GameAnalytics for game: " + event.getGameId());
                
            } catch (Exception e) {
                System.err.println("Error saving GameAnalytics: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
