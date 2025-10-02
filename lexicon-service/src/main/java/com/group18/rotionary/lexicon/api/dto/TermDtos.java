package com.group18.rotionary.lexicon.api.dto;

import java.util.List;

public class TermDtos {

    public record CreateTermRequest(String word, String description, String createdBy, List<String> tags) {}

    public record UpdateTermRequest(String description, List<String> tags) {}

    public record DefinitionRequest(String meaning, String example, String createdBy) {}

    public record TermResponse(Long id, String word, String description, List<String> tags) {}
}


