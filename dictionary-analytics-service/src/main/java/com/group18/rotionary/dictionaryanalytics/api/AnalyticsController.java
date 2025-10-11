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

    @GetMapping("/analytics/top")
    public ResponseEntity<java.util.List<java.util.Map<String, Object>>> top(@RequestParam(defaultValue = "24h") String window,
                                                                             @RequestParam(defaultValue = "5") int limit) {
        java.time.LocalDateTime since = java.time.LocalDateTime.now().minusHours(parseHours(window));
        var page = PageRequest.of(0, Math.max(1, limit));
        var rows = queryRepo.findTopSince(since, page);
        var list = new java.util.ArrayList<java.util.Map<String, Object>>();
        for (var r : rows) {
            list.add(java.util.Map.of(
                    "termId", r.getTermId(),
                    "termWord", r.getTermWord(),
                    "queryCount", r.getCnt()
            ));
        }
        return ResponseEntity.ok(list);
    }

    private long parseHours(String window) {
        if (window != null && window.endsWith("h")) {
            try { return Long.parseLong(window.substring(0, window.length() - 1)); } catch (Exception ignored) {}
        }
        return 24L;
    }
}


