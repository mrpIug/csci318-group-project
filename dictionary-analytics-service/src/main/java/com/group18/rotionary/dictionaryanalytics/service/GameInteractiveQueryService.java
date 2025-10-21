package com.group18.rotionary.dictionaryanalytics.service;

import com.group18.rotionary.dictionaryanalytics.api.dto.WindowedGameStats;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// Service for querying game analytics stats from Kafka Streams state stores
@Service
public class GameInteractiveQueryService {

    private final InteractiveQueryService interactiveQueryService;
    
    @Value("${windowstore.game.name}")
    private String GAME_WINDOWSTORE_NAME;
    
    @Value("${windowstore.game.size.ms}")
    private long GAME_WINDOW_SIZE_MS;

    public GameInteractiveQueryService(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    // Get stats for each target word from the latest completed time window
    public List<WindowedGameStats> getWindowedGameStats() {
        List<WindowedGameStats> windowedGameStats = new ArrayList<>();
        long now = Instant.now().toEpochMilli();

        long currentStart = (now / GAME_WINDOW_SIZE_MS) * GAME_WINDOW_SIZE_MS;
        Instant[] ranges = new Instant[] {
                // previous (completed)
                Instant.ofEpochMilli(currentStart - GAME_WINDOW_SIZE_MS),
                Instant.ofEpochMilli(currentStart),
                Instant.ofEpochMilli(currentStart - 2 * GAME_WINDOW_SIZE_MS)
        };

        for (Instant from : ranges) {
            Instant to = from.plus(Duration.ofMillis(GAME_WINDOW_SIZE_MS));

            var gameCounts = getGameCounts(from, to);
            if (gameCounts.isEmpty()) {
                continue;
            }
            var winTotals = getWinTotals(from, to);
            var attemptTotals = getAttemptTotals(from, to);

            for (String targetWord : gameCounts.keySet()) {
                Long gameCount = gameCounts.get(targetWord);
                Double winTotal = winTotals.getOrDefault(targetWord, 0.0);
                Double attemptTotal = attemptTotals.getOrDefault(targetWord, 0.0);

                Double winRate = gameCount > 0 ? winTotal / gameCount : 0.0;
                Double avgAttempts = gameCount > 0 ? attemptTotal / gameCount : 0.0;

                WindowedGameStats stats = new WindowedGameStats();
                stats.setTargetWord(targetWord);
                stats.setGameCount(gameCount);
                stats.setWinRate(Math.round(winRate * 100.0) / 100.0);
                stats.setAverageAttempts(Math.round(avgAttempts * 100.0) / 100.0);
                stats.setWindowInfo("window: " + from + " - " + to);
                windowedGameStats.add(stats);
            }

            if (!windowedGameStats.isEmpty()) {
                break; // stop at first non-empty window
            }
        }

        return windowedGameStats;
    }

    // Get overall win rate from the latest completed window
    public Double getWindowedWinRate() {
        List<WindowedGameStats> stats = getWindowedGameStats();
        if (stats.isEmpty()) {
            return 0.0;
        }
        
        long totalGames = stats.stream().mapToLong(WindowedGameStats::getGameCount).sum();
        double totalWins = stats.stream().mapToDouble(s -> s.getWinRate() * s.getGameCount()).sum();
        
        return totalGames > 0 ? Math.round((totalWins / totalGames) * 100.0) / 100.0 : 0.0;
    }

    // Get most popular target words from the latest completed window
    public List<WindowedGameStats> getWindowedPopularTargets() {
        List<WindowedGameStats> stats = getWindowedGameStats();
        return stats.stream()
                .sorted((s1, s2) -> Long.compare(s2.getGameCount(), s1.getGameCount()))
                .toList();
    }

    // Helper method to get game counts from the window store
    private java.util.Map<String, Long> getGameCounts(Instant timeFrom, Instant timeTo) {
        java.util.Map<String, Long> counts = new java.util.HashMap<>();
        try (KeyValueIterator<Windowed<String>, Long> iterator = getGameCountsStore().fetchAll(timeFrom, timeTo)) {
            while (iterator.hasNext()) {
                KeyValue<Windowed<String>, Long> kv = iterator.next();
                counts.put(kv.key.key(), kv.value);
            }
        }
        return counts;
    }

    // Helper method to get win totals from the window store
    private java.util.Map<String, Double> getWinTotals(Instant timeFrom, Instant timeTo) {
        java.util.Map<String, Double> totals = new java.util.HashMap<>();
        try (KeyValueIterator<Windowed<String>, Double> iterator = getWinTotalsStore().fetchAll(timeFrom, timeTo)) {
            while (iterator.hasNext()) {
                KeyValue<Windowed<String>, Double> kv = iterator.next();
                totals.put(kv.key.key(), kv.value);
            }
        }
        return totals;
    }

    // Helper method to get attempt totals from the window store
    private java.util.Map<String, Double> getAttemptTotals(Instant timeFrom, Instant timeTo) {
        java.util.Map<String, Double> totals = new java.util.HashMap<>();
        try (KeyValueIterator<Windowed<String>, Double> iterator = getAttemptTotalsStore().fetchAll(timeFrom, timeTo)) {
            while (iterator.hasNext()) {
                KeyValue<Windowed<String>, Double> kv = iterator.next();
                totals.put(kv.key.key(), kv.value);
            }
        }
        return totals;
    }

    // Helper method to retrieve the read-only window store for game counts
    private ReadOnlyWindowStore<String, Long> getGameCountsStore() {
        return this.interactiveQueryService.getQueryableStore(GAME_WINDOWSTORE_NAME + "-counts",
                QueryableStoreTypes.windowStore());
    }

    // Helper method to retrieve the read-only window store for win totals
    private ReadOnlyWindowStore<String, Double> getWinTotalsStore() {
        return this.interactiveQueryService.getQueryableStore(GAME_WINDOWSTORE_NAME + "-wins",
                QueryableStoreTypes.windowStore());
    }

    // Helper method to retrieve the read-only window store for attempt totals
    private ReadOnlyWindowStore<String, Double> getAttemptTotalsStore() {
        return this.interactiveQueryService.getQueryableStore(GAME_WINDOWSTORE_NAME + "-attempts",
                QueryableStoreTypes.windowStore());
    }
}

