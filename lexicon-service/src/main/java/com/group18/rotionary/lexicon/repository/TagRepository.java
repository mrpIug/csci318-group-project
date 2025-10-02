package com.group18.rotionary.lexicon.repository;

import com.group18.rotionary.lexicon.domain.valueobjects.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}


