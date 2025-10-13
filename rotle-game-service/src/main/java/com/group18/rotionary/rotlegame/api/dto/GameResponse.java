package com.group18.rotionary.rotlegame.api.dto;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import com.group18.rotionary.rotlegame.domain.entities.Attempt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// data transfer object for rotle game responses, hides the target word from the user
public record GameResponse(
    Long id,
    LocalDate gameDate,
    Integer maxAttempts,
    Integer currentAttempt,
    String gameStatus,
    String userSession,
    LocalDateTime createdAt,
    LocalDateTime completedAt,
    List<Attempt> attempts,
    Integer remainingAttempts,
    String targetWord,
    List<Character> availableLetters
) {
    public static GameResponse fromGame(Game game) {
        return new GameResponse(
            game.getId(),
            game.getGameDate(),
            game.getMaxAttempts(),
            game.getCurrentAttempt(),
            game.getGameStatus().name(),
            game.getUserSession(),
            game.getCreatedAt(),
            game.getCompletedAt(),
            game.getAttempts(),
            game.getRemainingAttempts(),
            game.isGameOver() ? game.getTargetWord() : "WILL BE REVEALED WHEN GAME ENDS",
            game.getAvailableLetters()
        );
    }
}

