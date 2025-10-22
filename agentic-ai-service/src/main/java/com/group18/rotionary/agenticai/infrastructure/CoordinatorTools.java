package com.group18.rotionary.agenticai.infrastructure;

import com.group18.rotionary.agenticai.service.EtymologyAgent;
import com.group18.rotionary.agenticai.service.SentenceGenerationAgent;
import com.group18.rotionary.agenticai.service.TagSuggestionAgent;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.Result;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CoordinatorTools {

    private final ApplicationContext applicationContext;

    public CoordinatorTools(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Tool("Consult the etymology specialist for detailed information about word etymology: origins, historical background, cultural context, how meanings evolved over time, and internet/brainrot usage patterns. This specialist is an expert in linguistics and modern slang etymology.")
    public String consultEtymologySpecialist(String sessionId, String query) {
        EtymologyAgent agent = applicationContext.getBean(EtymologyAgent.class);
        Result<String> result = agent.chat(sessionId, query);
        return result.content();
    }

    @Tool("Consult the sentence generation specialist to create example sentences showing how terms are used in context. This specialist can generate sentences with different tones (formal, casual, humorous, sarcastic), contexts (social media, work, texting, conversation, gaming), and for different audiences (gen-z, millennials, general, professional).")
    public String consultSentenceSpecialist(String sessionId, String query) {
        SentenceGenerationAgent agent = applicationContext.getBean(SentenceGenerationAgent.class);
        Result<String> result = agent.chat(sessionId, query);
        return result.content();
    }

    @Tool("Consult the tagging specialist for suggestions on relevant tags for slang terms. This specialist analyses words and suggests tags for formality level, context categories, usage type, and cultural context. Use this when users want help categorising or organising terms.")
    public String consultTagSpecialist(String sessionId, String query) {
        TagSuggestionAgent agent = applicationContext.getBean(TagSuggestionAgent.class);
        Result<String> result = agent.chat(sessionId, query);
        return result.content();
    }
}

