package com.group18.rotionary.dictionarypatron.repository;

import com.group18.rotionary.dictionarypatron.domain.entities.QueryEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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
}


