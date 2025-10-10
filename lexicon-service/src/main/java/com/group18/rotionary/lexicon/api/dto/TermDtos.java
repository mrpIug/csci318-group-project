package com.group18.rotionary.lexicon.api.dto;

import java.util.List;

public class TermDtos {

    public record CreateTermRequest(String word, String createdBy, List<String> tags) {}

    public record UpdateTermRequest(List<String> tags) {}

    public record DefinitionRequest(String meaning, String createdBy) {}

    public record TermResponse(Long id, String word, List<String> tags) {}
}


