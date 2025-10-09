package com.group18.rotionary.agenticai.api;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final ChatLanguageModel chat;

    public AiController(ChatLanguageModel chat) {
        this.chat = chat;
    }

    @PostMapping("/example-sentences")
    public ResponseEntity<Map<String, Object>> exampleSentences(@RequestBody Map<String, Object> body) {
        String term = String.valueOf(body.get("term"));
        String prompt = 
            "You are an expert in modern slang and internet language. " +
            "Your task is to generate realistic example sentences showing how slang is used in context.\n" +
            "Guidelines:\n" +
            "- Create 3 natural example sentences\n" +
            "- Show different contexts and situations\n" +
            "- Include both casual and more formal usage when appropriate\n" +
            "- Make examples relatable and realistic\n" +
            "- Show proper grammar and punctuation\n" +
            "- Avoid offensive or inappropriate content\n" +
            "- Format as numbered list with periods between examples\n" +
            "- Do not use quotation marks\n" +
            "- Do not use line breaks, separate examples with periods\n" +
            "Generate 3 concise example sentences that correctly use the slang term " + term + ".";
        String content = chat.generate(prompt).content().text();
        return ResponseEntity.ok(Map.of("result", content));
    }

    @PostMapping("/etymology")
    public ResponseEntity<Map<String, Object>> etymology(@RequestBody Map<String, Object> body) {
        String term = String.valueOf(body.get("term"));
        String prompt = 
            "You are an expert linguist specializing in modern slang and internet language. " +
            "Your task is to provide detailed etymology explanations for slang terms.\n" +
            "Guidelines:\n" +
            "- Focus on the origin and evolution of slang terms\n" +
            "- Include cultural context and usage patterns\n" +
            "- Mention any known sources (social media, communities, etc.)\n" +
            "- Be informative but accessible\n" +
            "- If uncertain about origins, acknowledge limitations\n" +
            "- Keep explanations concise but comprehensive (1 paragraph max)\n" +
            "- Use periods to separate sentences, avoid line breaks\n" +
            "- Do not use quotation marks\n" +
            "- Do not use line breaks, separate sentences with periods\n" +
            "Explain the etymology and timeline for the slang term " + term + ". Provide sections: origin, evolution timeline, notable usage.";
        String content = chat.generate(prompt).content().text();
        return ResponseEntity.ok(Map.of("result", content));
    }

    @PostMapping("/suggest-tags")
    public ResponseEntity<Map<String, Object>> suggestTags(@RequestBody Map<String, Object> body) {
        String word = String.valueOf(body.get("word"));
        String description = String.valueOf(body.get("description"));
        String prompt = 
            "You are an expert in categorizing modern slang and internet language. " +
            "Your task is to suggest appropriate tags/categories for slang terms.\n" +
            "Guidelines:\n" +
            "- Suggest 3 relevant tags\n" +
            "- Include formality level (formal, informal, slang, vulgar)\n" +
            "- Include context categories (social-media, gaming, gen-z, millennial, etc.)\n" +
            "- Include usage type (adjective, verb, noun, exclamation, etc.)\n" +
            "- Include cultural context when relevant\n" +
            "- Use comma-separated format\n" +
            "- Be specific but concise\n" +
            "- Do not use quotation marks\n" +
            "- Do not use line breaks, separate tags with commas\n" +
            "Suggest 3-6 concise tags for the slang term '" + word + "' described as '" + description + "'. Return a JSON array named 'tags'.";
        String content = chat.generate(prompt).content().text();
        return ResponseEntity.ok(Map.of("result", content));
    }
}


