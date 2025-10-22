package com.group18.rotionary.agenticai.api;

import com.group18.rotionary.agenticai.service.CoordinatorAgent;
import com.group18.rotionary.agenticai.service.TagSuggestionAgent;
import com.group18.rotionary.agenticai.service.SentenceGenerationAgent;
import com.group18.rotionary.agenticai.service.EtymologyAgent;
import dev.langchain4j.service.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final CoordinatorAgent coordinatorAgent;
    private final TagSuggestionAgent tagSuggestionAgent;
    private final SentenceGenerationAgent sentenceGenerationAgent;
    private final EtymologyAgent etymologyAgent;

    public AiController(CoordinatorAgent coordinatorAgent,
                       TagSuggestionAgent tagSuggestionAgent,
                       SentenceGenerationAgent sentenceGenerationAgent,
                       EtymologyAgent etymologyAgent) {
        this.coordinatorAgent = coordinatorAgent;
        this.tagSuggestionAgent = tagSuggestionAgent;
        this.sentenceGenerationAgent = sentenceGenerationAgent;
        this.etymologyAgent = etymologyAgent;
    }

    // NEW: Unified multi-agent endpoint using hierarchical coordinator
    @GetMapping("/chat")
    public String chat(@RequestParam String sessionId, @RequestParam String userMessage) {
        Result<String> result = coordinatorAgent.chat(sessionId, userMessage);
        return result.content();
    }

    // Legacy endpoints: Direct access to specialised agents (kept for backward compatibility)
    @GetMapping("/tag-agent")
    public String tagAgent(@RequestParam String sessionId, @RequestParam String userMessage) {
        Result<String> result = tagSuggestionAgent.chat(sessionId, userMessage);
        return result.content();
    }

    @GetMapping("/sentence-agent")
    public String sentenceAgent(@RequestParam String sessionId, @RequestParam String userMessage) {
        Result<String> result = sentenceGenerationAgent.chat(sessionId, userMessage);
        return result.content();
    }

    @GetMapping("/etymology-agent")
    public String etymologyAgent(@RequestParam String sessionId, @RequestParam String userMessage) {
        Result<String> result = etymologyAgent.chat(sessionId, userMessage);
        return result.content();
    }
}


