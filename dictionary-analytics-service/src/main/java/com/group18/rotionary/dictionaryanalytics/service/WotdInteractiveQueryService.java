package com.group18.rotionary.dictionaryanalytics.service;

import com.group18.rotionary.dictionaryanalytics.api.dto.WindowedTermStats;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// Service for querying Word of the Day analytics from Kafka Streams state stores
@Service
public class WotdInteractiveQueryService {

    private final InteractiveQueryService interactiveQueryService;
    
    @Value("${windowstore.wotd.name}")
    private String WOTD_WINDOWSTORE_NAME;
    
    @Value("${windowstore.wotd.size.ms}")
    private long WOTD_WINDOW_SIZE_MS;

    public WotdInteractiveQueryService(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    // Fetches the term query counts for each term from the most recently completed time window
    public List<WindowedTermStats> getWindowedTermQueries() {
        List<WindowedTermStats> results = new ArrayList<>();
        long now = Instant.now().toEpochMilli();
    
        long currentStart = (now / WOTD_WINDOW_SIZE_MS) * WOTD_WINDOW_SIZE_MS;
    
        // Check windows in order
        long[] starts = new long[] {
            currentStart - WOTD_WINDOW_SIZE_MS,  // most recent completed
            currentStart,                        // current (in-progress)
            currentStart - 2 * WOTD_WINDOW_SIZE_MS,
            currentStart - 3 * WOTD_WINDOW_SIZE_MS
        };
    
        for (long start : starts) {
            Instant from = Instant.ofEpochMilli(start);
            Instant to = Instant.ofEpochMilli(start + WOTD_WINDOW_SIZE_MS);
            try (KeyValueIterator<Windowed<String>, Long> it = getWotdWindowStore().fetchAll(from, to)) {
                boolean any = false;
                while (it.hasNext()) {
                    var kv = it.next();
                    WindowedTermStats s = new WindowedTermStats();
                    s.setTermWord(kv.key.key());
                    s.setQueryCount(kv.value);
                    s.setWindowInfo("window: " + kv.key.window().startTime() + " - " + kv.key.window().endTime());
                    results.add(s);
                    any = true;
                }
                if (any) break; // stop at the first non-empty window
            }
        }
        return results;
    }

    // Gets the most queried term from the most recently completed window for the word of the day
    public WindowedTermStats getCurrentWindowedWotd() {
        List<WindowedTermStats> stats = getWindowedTermQueries();
        return stats.stream()
                .max((s1, s2) -> Long.compare(s1.getQueryCount(), s2.getQueryCount()))
                .orElse(null);
    }

    // Helper method to retrieve the read-only window store for word of the day analytics
    private ReadOnlyWindowStore<String, Long> getWotdWindowStore() {
        return this.interactiveQueryService.getQueryableStore(WOTD_WINDOWSTORE_NAME,
                QueryableStoreTypes.windowStore());
    }
}

