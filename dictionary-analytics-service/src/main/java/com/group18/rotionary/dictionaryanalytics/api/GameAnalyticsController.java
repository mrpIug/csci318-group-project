package com.group18.rotionary.dictionaryanalytics.api;

import com.group18.rotionary.dictionaryanalytics.domain.entities.GameAnalytics;
import com.group18.rotionary.dictionaryanalytics.repository.GameAnalyticsRepository;
import com.group18.rotionary.dictionaryanalytics.service.GameInteractiveQueryService;
import com.group18.rotionary.dictionaryanalytics.api.dto.WindowedGameStats;
import com.group18.rotionary.dictionaryanalytics.api.dto.WindowedPlayerStats;
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
    private final GameInteractiveQueryService gameInteractiveQueryService;

    public GameAnalyticsController(
        GameAnalyticsRepository gameAnalyticsRepository,
        GameInteractiveQueryService gameInteractiveQueryService) {
        this.gameAnalyticsRepository = gameAnalyticsRepository;
        this.gameInteractiveQueryService = gameInteractiveQueryService;
    }

    // endpoint for all-time historical rotle game data
    @GetMapping("/dashboard/historical")
    public ResponseEntity<Map<String, Object>> getHistoricalDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // Overall stats
            GameAnalyticsRepository.GameStats stats = gameAnalyticsRepository.getOverallStats();
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalGames", stats.getTotalGames());
            overview.put("wonGames", stats.getWonGames());
            overview.put("winRate", Math.round(stats.getWinRate() * 100.0) / 100.0);
            overview.put("averageAttempts", Math.round(stats.getAverageAttempts() * 100.0) / 100.0);
            overview.put("source", "all time");
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

    // endpoint for real-time rotle game data
    @GetMapping("/dashboard/realtime")
    public ResponseEntity<Map<String, Object>> getRealtimeDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // Overall stats from current window
            Long totalGames = gameInteractiveQueryService.getWindowedTotalGames();
            Double winRate = gameInteractiveQueryService.getWindowedWinRate();
            Double avgAttempts = gameInteractiveQueryService.getWindowedAverageAttempts();
            
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalGames", totalGames);
            overview.put("winRate", winRate);
            overview.put("averageAttempts", avgAttempts);
            overview.put("source", "time windowed");
            dashboard.put("overview", overview);
            
            // Popular targets from current window (top 5)
            List<WindowedGameStats> popularTargets = gameInteractiveQueryService.getWindowedPopularTargets();
            dashboard.put("popularTargets", popularTargets.stream().limit(5).toList());
            
            // Top players from current window (top 5)
            List<WindowedPlayerStats> topPlayers = gameInteractiveQueryService.getWindowedPlayerStats();
            dashboard.put("topPlayers", topPlayers.stream()
                .sorted((p1, p2) -> Double.compare(p2.getWinRate(), p1.getWinRate()))
                .limit(5).toList());
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}