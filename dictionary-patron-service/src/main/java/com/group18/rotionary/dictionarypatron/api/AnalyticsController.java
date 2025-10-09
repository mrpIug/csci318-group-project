package com.group18.rotionary.dictionarypatron.api;

import com.group18.rotionary.dictionarypatron.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionarypatron.repository.DailyWotdRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wotd")
public class AnalyticsController {

    private final DailyWotdRepository repository;

    public AnalyticsController(DailyWotdRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/current")
    public ResponseEntity<DailyWOTD> current() {
        return repository.findTopByOrderByDateDesc()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}


