package com.group18.rotionary.dictionaryanalytics.repository;

import com.group18.rotionary.dictionaryanalytics.domain.entities.DailyWOTD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyWotdRepository extends JpaRepository<DailyWOTD, Long> {
    Optional<DailyWOTD> findTopByOrderByDateDesc();
    Optional<DailyWOTD> findByDate(LocalDate date);
}


