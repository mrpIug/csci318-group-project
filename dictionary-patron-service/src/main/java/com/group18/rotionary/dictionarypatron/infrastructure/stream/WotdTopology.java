package com.group18.rotionary.dictionarypatron.infrastructure.stream;

import com.group18.rotionary.shared.domain.events.TermQueriedEvent;
import com.group18.rotionary.shared.domain.events.WordOfTheDayUpdatedEvent;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;

@Configuration
public class WotdTopology {

    @Bean
    public java.util.function.Function<KStream<String, TermQueriedEvent>, KStream<String, WordOfTheDayUpdatedEvent>> computeWotd() {
        return input -> input
                .selectKey((k, v) -> v.getTermWord())
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5)))
                .count()
                .toStream()
                .map((windowedKey, count) -> KeyValue.pair(windowedKey.window().endTime().toString(), new WordOfTheDayUpdatedEvent(
                        null,
                        windowedKey.key(),
                        count,
                        LocalDate.now().toString()
                )));
    }
}


