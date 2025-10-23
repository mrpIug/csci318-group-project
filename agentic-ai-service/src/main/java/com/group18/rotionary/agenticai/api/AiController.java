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
        // inject sessionId into the message context so gemini can actually see and use it without hallucinating
        String contextualMessage = String.format(
            "[SYSTEM CONTEXT: Your sessionId for this conversation is '%s'. Use this EXACT value when calling specialist tools.] User message: %s",
            sessionId,
            userMessage
        );
        Result<String> result = coordinatorAgent.chat(sessionId, contextualMessage);
        return result.content();
    }
}

