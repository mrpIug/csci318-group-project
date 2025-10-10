package com.group18.rotionary.agenticai.api;

import com.group18.rotionary.agenticai.service.AiUseCases;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiUseCases ai;

    public AiController(AiUseCases ai) {
        this.ai = ai;
    }

    @PostMapping("/example-sentences")
    public ResponseEntity<Map<String, Object>> exampleSentences(@RequestBody Map<String, Object> body) {
        String term = String.valueOf(body.get("term"));
        String content = ai.exampleSentences(term);
        return ResponseEntity.ok(Map.of("result", content));
    }

    @PostMapping("/etymology")
    public ResponseEntity<Map<String, Object>> etymology(@RequestBody Map<String, Object> body) {
        String term = String.valueOf(body.get("term"));
        String content = ai.etymology(term);
        return ResponseEntity.ok(Map.of("result", content));
    }

    @PostMapping("/suggest-tags")
    public ResponseEntity<Map<String, Object>> suggestTags(@RequestBody Map<String, Object> body) {
        String word = String.valueOf(body.get("word"));
        String content = ai.suggestTags(word);
        return ResponseEntity.ok(Map.of("result", content));
    }
}


