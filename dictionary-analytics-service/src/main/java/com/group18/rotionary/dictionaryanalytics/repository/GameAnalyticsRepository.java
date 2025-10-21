package com.group18.rotionary.dictionaryanalytics.repository;

import com.group18.rotionary.dictionaryanalytics.domain.entities.GameAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameAnalyticsRepository extends JpaRepository<GameAnalytics, Long> {

    // Interface for aggregated game statistics
    interface GameStats {
        Long getTotalGames();
        Long getWonGames();
        Double getWinRate();
        Double getAverageAttempts();
    }

    interface PopularTargetWord {
        String getTargetWord();
        Long getGameCount();
    }

    interface PlayerStats {
        String getUserSession();
        Long getTotalGames();
        Long getWonGames();
        Double getWinRate();
        Double getAverageAttempts();
    }

    // Find game analytics by game ID
    Optional<GameAnalytics> findByGameId(Long gameId);

    // Get overall game statistics
    @Query("SELECT " +
           "COUNT(g) as totalGames, " +
           "COALESCE(SUM(CASE WHEN g.won = true THEN 1 ELSE 0 END), 0) as wonGames, " +
           "CASE WHEN COUNT(g) > 0 THEN CAST(COALESCE(SUM(CASE WHEN g.won = true THEN 1 ELSE 0 END), 0) AS DOUBLE) / COUNT(g) ELSE 0.0 END as winRate, " +
           "COALESCE(AVG(g.attemptsCount), 0.0) as averageAttempts " +
           "FROM GameAnalytics g")
    GameStats getOverallStats();

    // Get game statistics since a specific time
    @Query("SELECT " +
           "COUNT(g) as totalGames, " +
           "COALESCE(SUM(CASE WHEN g.won = true THEN 1 ELSE 0 END), 0) as wonGames, " +
           "CASE WHEN COUNT(g) > 0 THEN CAST(COALESCE(SUM(CASE WHEN g.won = true THEN 1 ELSE 0 END), 0) AS DOUBLE) / COUNT(g) ELSE 0.0 END as winRate, " +
           "COALESCE(AVG(g.attemptsCount), 0.0) as averageAttempts " +
           "FROM GameAnalytics g WHERE g.completedAt >= :since")
    GameStats getStatsSince(@Param("since") LocalDateTime since);

    // Get most popular target words
    @Query("SELECT g.targetWord as targetWord, COUNT(g) as gameCount " +
           "FROM GameAnalytics g " +
           "GROUP BY g.targetWord " +
           "ORDER BY COUNT(g) DESC")
    List<PopularTargetWord> getPopularTargetWords();

    // Get top players by win rate (minimum 1 game)
    @Query("SELECT g.userSession as userSession, " +
           "COUNT(g) as totalGames, " +
           "SUM(CASE WHEN g.won = true THEN 1 ELSE 0 END) as wonGames, " +
           "CAST(SUM(CASE WHEN g.won = true THEN 1 ELSE 0 END) AS DOUBLE) / COUNT(g) as winRate, " +
           "AVG(g.attemptsCount) as averageAttempts " +
           "FROM GameAnalytics g " +
           "GROUP BY g.userSession " +
           "HAVING COUNT(g) >= 1 " +
           "ORDER BY winRate DESC, COUNT(g) DESC")
    List<PlayerStats> getTopPlayers();

    // Get recent games (last 10)
    List<GameAnalytics> findTop10ByOrderByCompletedAtDesc();

    // Count games by user session
    long countByUserSession(String userSession);

    // Count won games by user session
    long countByUserSessionAndWon(String userSession, boolean won);
}
