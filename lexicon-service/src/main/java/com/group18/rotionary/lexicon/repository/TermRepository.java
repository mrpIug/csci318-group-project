package com.group18.rotionary.lexicon.repository;

import com.group18.rotionary.lexicon.domain.aggregates.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {

    Optional<Term> findByWord(String word);

    @Query("select t from Term t join t.tags tag where tag.name = :tagName")
    List<Term> findByTagName(@Param("tagName") String tagName);
}


