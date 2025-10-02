package com.group18.rotionary.lexicon.repository;

import com.group18.rotionary.lexicon.domain.entities.Definition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefinitionRepository extends JpaRepository<Definition, Long> {
    List<Definition> findByTermId(Long termId);
}


