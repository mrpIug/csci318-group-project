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

    public Tools(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Tool("Add a tag to a term in the lexicon. Use this when the user confirms they want to add a specific tag.")
    @SuppressWarnings("unchecked")
    public String addTagToTerm(Long termId, String tagName) {
        try {
            // grab current term to see existing tags
            String getUrl = LEXICON_SERVICE_URL + "/" + termId;
            Map<String, Object> term = restTemplate.getForObject(getUrl, Map.class);

            if (term == null) {
                return "Error: Term with ID " + termId + " not found.";
            }

            List<String> tags = (List<String>) term.get("tags");
            if (tags == null) {
                tags = List.of(tagName.toLowerCase().trim());
            } else {
                if (tags.contains(tagName.toLowerCase().trim())) {
                    return "Tag '" + tagName + "' already exists on this term.";
                }
                tags = new java.util.ArrayList<>(tags);
                tags.add(tagName.toLowerCase().trim());
            }

            // send update with new tag list
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

    @Tool("Search for a term by its word and return its detailed information, including word, term ID, tags, main definition, and who created it.")
    @SuppressWarnings("unchecked")
    public String getTermDetailsByWord(String word) {
        try {
            String searchUrl = LEXICON_SERVICE_URL + "/search?word=" + word;
            List<Map<String, Object>> terms = restTemplate.getForObject(searchUrl, List.class);

            // try again once if term not found
            if ((terms == null || terms.isEmpty())) {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                terms = restTemplate.getForObject(searchUrl, List.class);
            }

            if (terms == null || terms.isEmpty()) {
                return "No term found with word '" + word + "'.";
            }

            Map<String, Object> term = terms.get(0);
            Long id = term.get("id") != null ? ((Number) term.get("id")).longValue() : null;
            String foundWord = (String) term.get("word");
            List<String> tags = (List<String>) term.get("tags");
            Map<String, Object> definition = (Map<String, Object>) term.get("definition");
            String createdBy = (String) term.get("createdBy");

            StringBuilder result = new StringBuilder();
            result.append("Word: ").append(foundWord != null ? foundWord : "unknown").append("\n");
            result.append("Term ID: ").append(id != null ? id : "unknown").append("\n");
            result.append("Tags: ").append(tags != null && !tags.isEmpty() ? String.join(", ", tags) : "none").append("\n");
            if (definition != null && definition.get("meaning") != null) {
                result.append("Definition: ").append(definition.get("meaning"));
            } else {
                result.append("Definition: none");
            }
            result.append("\n");
            result.append("Created by: ").append(createdBy != null ? createdBy : "unknown");

            return result.toString();
        } catch (Exception e) {
            return "Error searching for term: " + e.getMessage();
        }
    }

    @Tool("Create a new term in the lexicon with a word, definition, and username. Use when the user wants to add a new slang term.")
    @SuppressWarnings("unchecked")
    public String createTerm(String word, String definition, String username) {
        try {
            // check if term already exists
            String searchUrl = LEXICON_SERVICE_URL + "/search?word=" + word;
            List<Map<String, Object>> existingTerms = restTemplate.getForObject(searchUrl, List.class);

            if (existingTerms != null && !existingTerms.isEmpty()) {
                return "Term '" + word + "' already exists in the lexicon. Cannot create duplicate.";
            }

            // create the term
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> createTermRequest = Map.of(
                "word", word,
                "createdBy", username
            );

            HttpEntity<Map<String, Object>> termEntity = new HttpEntity<>(createTermRequest, headers);
            Map<String, Object> createdTerm = restTemplate.postForObject(LEXICON_SERVICE_URL, termEntity, Map.class);

            if (createdTerm == null) {
                return "Error: Failed to create term '" + word + "'.";
            }

            Long termId = ((Number) createdTerm.get("id")).longValue();

            // add the definition
            String definitionUrl = LEXICON_SERVICE_URL + "/" + termId + "/definitions";
            Map<String, Object> createDefinitionRequest = Map.of(
                "meaning", definition,
                "createdBy", username
            );

            HttpEntity<Map<String, Object>> defEntity = new HttpEntity<>(createDefinitionRequest, headers);
            restTemplate.postForObject(definitionUrl, defEntity, Map.class);

            // small delay to let the database catch up after term creation
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Successfully created term '" + word + "' (ID: " + termId + ") with definition: " + definition;
        } catch (Exception e) {
            return "Error creating term: " + e.getMessage();
        }
    }
}
