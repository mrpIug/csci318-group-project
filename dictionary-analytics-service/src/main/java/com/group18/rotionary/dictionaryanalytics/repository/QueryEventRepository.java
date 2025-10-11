package com.group18.rotionary.dictionaryanalytics.repository;

import com.group18.rotionary.dictionaryanalytics.domain.entities.QueryEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueryEventRepository extends JpaRepository<QueryEvent, Long> {

    interface TopTermCount {
        Long getTermId();
        String getTermWord();
        Long getCnt();
    }

    @Query("select e.termId as termId, e.termWord as termWord, count(e) as cnt " +
           "from QueryEvent e " +
           "where e.queryTimestamp >= :since " +
           "group by e.termId, e.termWord " +
           "order by cnt desc")
    List<TopTermCount> findTopSince(@Param("since") LocalDateTime since, Pageable pageable);
    
    @Query("select e.termId from QueryEvent e where e.termWord = :termWord order by e.queryTimestamp desc limit 1")
    Optional<Long> findTermIdByWord(@Param("termWord") String termWord);
}


