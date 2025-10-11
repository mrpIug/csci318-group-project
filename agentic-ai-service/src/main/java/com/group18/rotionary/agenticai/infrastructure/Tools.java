package com.group18.rotionary.agenticai.infrastructure;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class Tools {

    private final RestTemplate restTemplate;
    private static final String LEXICON_SERVICE_URL = "http://localhost:8081/api/terms";

    public Tools() {
        this.restTemplate = new RestTemplate();
    }

    @Tool("Add a tag to a term in the lexicon. Use this when the user confirms they want to add a specific tag.")
    @SuppressWarnings("unchecked")
    public String addTagToTerm(Long termId, String tagName) {
        try {
            // First, get the current term to get existing tags
            String getUrl = LEXICON_SERVICE_URL + "/" + termId;
            Map<String, Object> term = restTemplate.getForObject(getUrl, Map.class);
            
            if (term == null) {
                return "Error: Term with ID " + termId + " not found.";
            }
            
            // Get existing tags
            List<String> tags = (List<String>) term.get("tags");
            if (tags == null) {
                tags = List.of(tagName.toLowerCase().trim());
            } else {
                if (tags.contains(tagName.toLowerCase().trim())) {
                    return "Tag '" + tagName + "' already exists on this term.";
                }
                // Add new tag to existing list
                tags = new java.util.ArrayList<>(tags);
                tags.add(tagName.toLowerCase().trim());
            }
            
            // Update the term with the new tags
            String updateUrl = LEXICON_SERVICE_URL + "/" + termId;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> updateRequest = Map.of("tags", tags);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updateRequest, headers);
            
            restTemplate.exchange(updateUrl, HttpMethod.PUT, entity, Map.class);
            
            return "Successfully added tag '" + tagName + "' to term ID " + termId + ".";
        } catch (Exception e) {
            return "Error adding tag: " + e.getMessage();
        }
    }

    @Tool("Get details about a term by its ID, including the word, tags, and definition.")
    @SuppressWarnings("unchecked")
    public String getTermDetails(Long termId) {
        try {
            String url = LEXICON_SERVICE_URL + "/" + termId;
            Map<String, Object> term = restTemplate.getForObject(url, Map.class);
            
            if (term == null) {
                return "Term with ID " + termId + " not found.";
            }
            
            String word = (String) term.get("word");
            List<String> tags = (List<String>) term.get("tags");
            Map<String, Object> definition = (Map<String, Object>) term.get("definition");
            
            StringBuilder result = new StringBuilder();
            result.append("Term ID: ").append(termId).append("\n");
            result.append("Word: ").append(word).append("\n");
            result.append("Tags: ").append(tags != null && !tags.isEmpty() ? String.join(", ", tags) : "none").append("\n");
            
            if (definition != null) {
                result.append("Definition: ").append(definition.get("meaning"));
            } else {
                result.append("Definition: none");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Error getting term details: " + e.getMessage();
        }
    }

    @Tool("Search for a term by its word to get the term ID and other details.")
    @SuppressWarnings("unchecked")
    public String searchTermByWord(String word) {
        try {
            String url = LEXICON_SERVICE_URL + "/search?word=" + word;
            List<Map<String, Object>> terms = restTemplate.getForObject(url, List.class);
            
            if (terms == null || terms.isEmpty()) {
                return "No term found with word '" + word + "'.";
            }
            
            Map<String, Object> term = terms.get(0);
            Long id = ((Number) term.get("id")).longValue();
            String foundWord = (String) term.get("word");
            List<String> tags = (List<String>) term.get("tags");
            
            StringBuilder result = new StringBuilder();
            result.append("Found term: ").append(foundWord).append("\n");
            result.append("Term ID: ").append(id).append("\n");
            result.append("Tags: ").append(tags != null && !tags.isEmpty() ? String.join(", ", tags) : "none");
            
            return result.toString();
        } catch (Exception e) {
            return "Error searching for term: " + e.getMessage();
        }
    }
}

