package com.group18.rotionary.rotlegame.api.dto;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import com.group18.rotionary.rotlegame.domain.entities.Attempt;

import java.time.LocalDateTime;
import java.util.List;

// data transfer object for attempt responses, includes updated available letters
public record AttemptResponse(
    Long id,
    String guess,
    Integer attemptNumber,
    String result,
    Boolean isCorrect,
    LocalDateTime createdAt,
    List<Character> availableLetters
) {
    public static AttemptResponse fromAttempt(Attempt attempt, Game game) {
        return new AttemptResponse(
            attempt.getId(),
            attempt.getGuess(),
            attempt.getAttemptNumber(),
            attempt.getResult(),
            attempt.getIsCorrect(),
            attempt.getCreatedAt(),
            game.getAvailableLetters()
        );
    }
}

