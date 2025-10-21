package com.group18.rotionary.dictionaryanalytics.api;

import com.group18.rotionary.dictionaryanalytics.api.dto.WindowedTermStats;
import com.group18.rotionary.dictionaryanalytics.service.WotdInteractiveQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalyticsController {

    private final WotdInteractiveQueryService wotdInteractiveQueryService;

    public AnalyticsController(WotdInteractiveQueryService wotdInteractiveQueryService) {
        this.wotdInteractiveQueryService = wotdInteractiveQueryService;
    }

    @GetMapping("/wotd/realtime")
    public ResponseEntity<WindowedTermStats> getRealtimeWotd() {
        WindowedTermStats realtimeWotd = wotdInteractiveQueryService.getCurrentWindowedWotd();
        if (realtimeWotd != null) {
            return ResponseEntity.ok(realtimeWotd);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

