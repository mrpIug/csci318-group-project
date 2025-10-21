package com.group18.rotionary.dictionaryanalytics.repository;

import com.group18.rotionary.dictionaryanalytics.domain.entities.QueryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface QueryEventRepository extends JpaRepository<QueryEvent, Long> {

    @Query("select e.termId from QueryEvent e where e.termWord = :termWord order by e.queryTimestamp desc limit 1")
    Optional<Long> findTermIdByWord(@Param("termWord") String termWord);
    
    @Query("select count(e) from QueryEvent e where e.termId = :termId and cast(e.queryTimestamp as date) = :date")
    long countByTermIdAndDate(@Param("termId") Long termId, @Param("date") LocalDate date);
}


