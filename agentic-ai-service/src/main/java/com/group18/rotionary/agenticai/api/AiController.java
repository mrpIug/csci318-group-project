package com.group18.rotionary.agenticai.api;

import com.group18.rotionary.agenticai.service.CoordinatorAgent;
import dev.langchain4j.service.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final CoordinatorAgent coordinatorAgent;

    public AiController(CoordinatorAgent coordinatorAgent) {
        this.coordinatorAgent = coordinatorAgent;
    }

    // multi-agent AI system endpoint
    @GetMapping("/chat")
    public String chat(@RequestParam String sessionId, @RequestParam String userMessage) {
        Result<String> result = coordinatorAgent.chat(sessionId, userMessage);
        return result.content();
    }
}

