package com.group18.rotionary.dictionaryanalytics.infrastructure.stream;

import com.group18.rotionary.dictionaryanalytics.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionaryanalytics.repository.DailyWotdRepository;
import com.group18.rotionary.dictionaryanalytics.repository.QueryEventRepository;
import com.group18.rotionary.shared.domain.events.TermQueriedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.function.Consumer;

@Configuration
public class WordOfTheDayUpdateConsumer {

    private final DailyWotdRepository dailyWotdRepository;
    private final QueryEventRepository queryEventRepository;

    public WordOfTheDayUpdateConsumer(DailyWotdRepository dailyWotdRepository, QueryEventRepository queryEventRepository) {
        this.dailyWotdRepository = dailyWotdRepository;
        this.queryEventRepository = queryEventRepository;
    }

    @Bean
    public Consumer<TermQueriedEvent> consumeWotdUpdates() {
        return event -> {
            try {
                LocalDate today = LocalDate.now();

                // Get current count for this term today
                long currentCount = queryEventRepository.countByTermIdAndDate(event.getTermId(), today);

                // Update/create WOTD based on current queries
                dailyWotdRepository.findByDate(today)
                        .ifPresentOrElse(
                                existing -> {
                                    // Get count for the current WOTD term
                                    long existingCount = queryEventRepository.countByTermIdAndDate(existing.getTermId(), today);

                                    if (event.getTermId().equals(existing.getTermId())) {
                                        existing.updateTerm(event.getTermId(), event.getTermWord(), currentCount);
                                        dailyWotdRepository.save(existing);
                                        System.out.println("SAME WOTD: " + event.getTermWord() + " (" + currentCount + " queries)");
                                    } else if (currentCount > existingCount) {
                                        existing.updateTerm(event.getTermId(), event.getTermWord(), currentCount);
                                        dailyWotdRepository.save(existing);
                                        System.out.println("NEW WOTD REPLACING OLD: " + event.getTermWord() + " (" + currentCount + " queries) replaces " 
                                               + existing.getTermWord() + " (" + existingCount + " queries) for today.");
                                    }
                                    
                                },
                                () -> {
                                    // Create new WOTD if none exists
                                    DailyWOTD newWotd = new DailyWOTD(today, event.getTermId(), event.getTermWord(), currentCount);
                                    dailyWotdRepository.save(newWotd);
                                    System.out.println("NEW WORD OF THE DAY: " + event.getTermWord() + " (" + currentCount + " queries) has been set for today.");
                                }
                        );
            } catch (Exception e) {
                System.err.println("Error updating WOTD: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}