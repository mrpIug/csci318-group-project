package com.group18.rotionary.rotlegame.api;

import com.group18.rotionary.rotlegame.api.dto.AttemptResponse;
import com.group18.rotionary.rotlegame.api.dto.GameResponse;
import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import com.group18.rotionary.rotlegame.domain.entities.Attempt;
import com.group18.rotionary.rotlegame.repository.GameRepository;
import com.group18.rotionary.rotlegame.service.RotleGameService;
import com.group18.rotionary.rotlegame.infrastructure.GameEventPublisher;
import com.group18.rotionary.shared.domain.events.GameCompletedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameRepository games;
    private final RotleGameService gameService;
    private final GameEventPublisher eventPublisher;

    public GameController(GameRepository games, RotleGameService gameService, GameEventPublisher eventPublisher) {
        this.games = games;
        this.gameService = gameService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/start")
    public ResponseEntity<GameResponse> start(@RequestBody Map<String, Object> body) {
        String session = String.valueOf(body.getOrDefault("userSession", "cli"));
        try {
            Game game = gameService.createNewGame(session);
            Game savedGame = games.save(game);
            return ResponseEntity.ok(GameResponse.fromGame(savedGame));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/guess")
    public ResponseEntity<AttemptResponse> guess(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Game> gameOpt = games.findById(id);
        if (gameOpt.isEmpty()) return ResponseEntity.notFound().build();
        
        Game game = gameOpt.get();
        Attempt attempt = game.makeAttempt(String.valueOf(body.get("guess")));
        games.save(game);
        
        // publish event if game is over
        if (game.isGameOver()) {
            GameCompletedEvent event = new GameCompletedEvent(
                    game.getId(),
                    game.getTargetWord(),
                    game.isWon(),
                    game.getCurrentAttempt().intValue(),
                    game.getUserSession()
            );
            eventPublisher.publishGameCompleted(event);
        }
        
        return ResponseEntity.ok(AttemptResponse.fromAttempt(attempt, game));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> get(@PathVariable Long id) {
        return games.findById(id)
                .map(GameResponse::fromGame)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


