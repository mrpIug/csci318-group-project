package com.group18.rotionary.dictionarypatron.infrastructure.stream;

import com.group18.rotionary.dictionarypatron.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionarypatron.repository.DailyWotdRepository;
import com.group18.rotionary.shared.domain.events.WordOfTheDayUpdatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.function.Consumer;

@Configuration
public class WotdPersistenceConsumer {

    private final DailyWotdRepository repository;

    public WotdPersistenceConsumer(DailyWotdRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Consumer<WordOfTheDayUpdatedEvent> persistWotd() {
        return evt -> {
            LocalDate date = LocalDate.parse(evt.getDate());
            repository.findByDate(date)
                    .ifPresentOrElse(existing -> {
                        existing.updateTerm(evt.getTermId(), evt.getTermWord(), evt.getQueryCount());
                        repository.save(existing);
                    }, () -> repository.save(new DailyWOTD(date, evt.getTermId(), evt.getTermWord(), evt.getQueryCount())));
        };
    }
}


