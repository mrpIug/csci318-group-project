package com.group18.rotionary.dictionaryanalytics.api;

import com.group18.rotionary.dictionaryanalytics.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionaryanalytics.repository.DailyWotdRepository;
import com.group18.rotionary.dictionaryanalytics.repository.QueryEventRepository;
import com.group18.rotionary.dictionaryanalytics.service.WordOfTheDayService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalyticsController {

    private final DailyWotdRepository repository;
    private final QueryEventRepository queryRepo;
    private final WordOfTheDayService wordOfTheDayService;

    public AnalyticsController(DailyWotdRepository repository, QueryEventRepository queryRepo, WordOfTheDayService wordOfTheDayService) {
        this.repository = repository;
        this.queryRepo = queryRepo;
        this.wordOfTheDayService = wordOfTheDayService;
    }

    @GetMapping("/wotd/current")
    public ResponseEntity<DailyWOTD> current() {
        DailyWOTD wotd = wordOfTheDayService.getCurrentWordOfTheDay();
        if (wotd != null) {
            return ResponseEntity.ok(wotd);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


