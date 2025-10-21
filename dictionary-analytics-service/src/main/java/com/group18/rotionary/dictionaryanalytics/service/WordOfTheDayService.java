package com.group18.rotionary.dictionaryanalytics.service;

import com.group18.rotionary.dictionaryanalytics.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionaryanalytics.repository.DailyWotdRepository;
import org.springframework.stereotype.Service;

@Service
public class WordOfTheDayService {

    private final DailyWotdRepository dailyWotdRepository;

    public WordOfTheDayService(DailyWotdRepository dailyWotdRepository) {
        this.dailyWotdRepository = dailyWotdRepository;
    }


    // gets current wotd
    public DailyWOTD getCurrentWordOfTheDay() {
        return dailyWotdRepository.findTopByOrderByDateDesc()
            .orElse(null);
    }
}
