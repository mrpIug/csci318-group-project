package com.group18.rotionary.rotlegame.api;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import com.group18.rotionary.rotlegame.domain.entities.Attempt;
import com.group18.rotionary.rotlegame.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameRepository games;

    public GameController(GameRepository games) {
        this.games = games;
    }

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Map<String, Object> body) {
        String targetWord = String.valueOf(body.get("targetWord"));
        Long targetWordId = Long.valueOf(String.valueOf(body.get("targetWordId")));
        String session = String.valueOf(body.getOrDefault("userSession", "cli"));
        Game game = new Game(targetWord, targetWordId, LocalDate.now(), session);
        return ResponseEntity.ok(games.save(game));
    }

    @PostMapping("/{id}/guess")
    public ResponseEntity<Attempt> guess(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Game> gameOpt = games.findById(id);
        if (gameOpt.isEmpty()) return ResponseEntity.notFound().build();
        Game game = gameOpt.get();
        Attempt attempt = game.makeAttempt(String.valueOf(body.get("guess")));
        games.save(game);
        return ResponseEntity.ok(attempt);
    }
}


