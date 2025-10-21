package com.group18.rotionary.dictionaryanalytics.api;

import com.group18.rotionary.dictionaryanalytics.domain.entities.GameAnalytics;
import com.group18.rotionary.dictionaryanalytics.repository.GameAnalyticsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game-stats")
public class GameAnalyticsController {

    private final GameAnalyticsRepository gameAnalyticsRepository;

    public GameAnalyticsController(GameAnalyticsRepository gameAnalyticsRepository) {
        this.gameAnalyticsRepository = gameAnalyticsRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // Overall stats
            GameAnalyticsRepository.GameStats stats = gameAnalyticsRepository.getOverallStats();
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalGames", stats.getTotalGames());
            overview.put("wonGames", stats.getWonGames());
            overview.put("winRate", Math.round(stats.getWinRate() * 100.0) / 100.0);
            overview.put("averageAttempts", Math.round(stats.getAverageAttempts() * 100.0) / 100.0);
            dashboard.put("overview", overview);
            
            // Popular target words (top 5)
            List<GameAnalyticsRepository.PopularTargetWord> popularWords = 
                gameAnalyticsRepository.getPopularTargetWords();
            dashboard.put("popularTargets", popularWords.stream().limit(5).toList());
            
            // Top players (top 5)
            List<GameAnalyticsRepository.PlayerStats> topPlayers = 
                gameAnalyticsRepository.getTopPlayers();
            dashboard.put("topPlayers", topPlayers.stream().limit(5).toList());
            
            // Recent games (last 5)
            List<GameAnalytics> recentGames = gameAnalyticsRepository.findTop10ByOrderByCompletedAtDesc();
            dashboard.put("recentGames", recentGames.stream().limit(5).toList());
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}