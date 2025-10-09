package com.group18.rotionary.dictionarypatron.api;

import com.group18.rotionary.dictionarypatron.domain.entities.DailyWOTD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wotd")
public class AnalyticsController {

    @GetMapping("/current")
    public ResponseEntity<DailyWOTD> current() {
        // Placeholder: In a full impl, load latest DailyWOTD from DB/state store
        return ResponseEntity.noContent().build();
    }
}


