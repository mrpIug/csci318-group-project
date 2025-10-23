package com.group18.rotionary.dictionaryanalytics.infrastructure.stream;

import com.group18.rotionary.shared.domain.events.GameCompletedEvent;
import com.group18.rotionary.shared.domain.events.TermQueriedEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.BiFunction;

// Spring configuration for processing analytics on term queries and game completions with Kafka Streams
@Configuration
public class CombinedAnalyticsStreamProcessor {

    @Value("${windowstore.wotd.name}")
    private String WOTD_WINDOWSTORE_NAME;

    @Value("${windowstore.wotd.size.ms}")
    private long WOTD_WINDOW_SIZE_MS;

    @Value("${windowstore.game.name}")
    private String GAME_WINDOWSTORE_NAME;

    @Value("${windowstore.game.size.ms}")
    private long GAME_WINDOW_SIZE_MS;

    // Processes term query and game completion streams for analytics
    @Bean
    public BiFunction<KStream<String, TermQueriedEvent>, KStream<String, GameCompletedEvent>, Void> processAnalytics() {
        return (termStream, gameStream) -> {

            // Process term queries for WOTD analytics
            KTable<Windowed<String>, Long> termQueryCounts = termStream
                    .filter((key, value) -> value != null)
                    .map((key, value) -> {
                        String termWord = value.getTermWord();
                        return KeyValue.pair(termWord, 1L);
                    })
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(WOTD_WINDOW_SIZE_MS)))
                    .count(
                            Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(WOTD_WINDOWSTORE_NAME)
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Long())
                    );

            // Process game completions for game analytics
            KTable<Windowed<String>, Long> gameCountsByTarget = gameStream
                    .filter((key, value) -> value != null) // Filter out null values
                    .map((key, value) -> {
                        String targetWord = value.getTargetWord();
                        return KeyValue.pair(targetWord, 1L);
                    })
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(GAME_WINDOW_SIZE_MS)))
                    .count(
                            Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(GAME_WINDOWSTORE_NAME + "-counts")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Long())
                    );

            // Win rate aggregation by target word
            KTable<Windowed<String>, Double> winRatesByTarget = gameStream
                    .filter((key, value) -> value != null) // Filter out null values
                    .map((key, value) -> {
                        String targetWord = value.getTargetWord();
                        double winValue = value.isWon() ? 1.0 : 0.0;
                        return KeyValue.pair(targetWord, winValue);
                    })
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(GAME_WINDOW_SIZE_MS)))
                    .aggregate(
                            () -> 0.0,
                            (key, value, aggregate) -> aggregate + value,
                            Materialized.<String, Double, WindowStore<Bytes, byte[]>>as(GAME_WINDOWSTORE_NAME + "-wins")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Double())
                    );

            // Average rotle game attempts aggregation by target word
            KTable<Windowed<String>, Double> avgAttemptsByTarget = gameStream
                    .filter((key, value) -> value != null) // Filter out null values
                    .map((key, value) -> {
                        String targetWord = value.getTargetWord();
                        return KeyValue.pair(targetWord, (double) value.getAttemptsCount());
                    })
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(GAME_WINDOW_SIZE_MS)))
                    .aggregate(
                            () -> 0.0,
                            (key, value, aggregate) -> aggregate + value,
                            Materialized.<String, Double, WindowStore<Bytes, byte[]>>as(GAME_WINDOWSTORE_NAME + "-attempts")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Double())
                    );

            termQueryCounts.toStream()
                    .print(Printed.<Windowed<String>, Long>toSysOut().withLabel("Windowed term query counts"));
            
            gameCountsByTarget.toStream()
                    .print(Printed.<Windowed<String>, Long>toSysOut().withLabel("Windowed game counts by target"));
            
            winRatesByTarget.toStream()
                    .print(Printed.<Windowed<String>, Double>toSysOut().withLabel("Windowed win totals by target"));
            
            avgAttemptsByTarget.toStream()
                    .print(Printed.<Windowed<String>, Double>toSysOut().withLabel("Windowed attempt totals by target"));

            // Player game counts
            KTable<Windowed<String>, Long> playerGameCounts = gameStream
                    .filter((key, value) -> value != null)
                    .map((key, value) -> KeyValue.pair(value.getUserSession(), 1L))
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(GAME_WINDOW_SIZE_MS)))
                    .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(GAME_WINDOWSTORE_NAME + "-player-counts")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(Serdes.Long()));

            // Player win totals
            KTable<Windowed<String>, Double> playerWinTotals = gameStream
                    .filter((key, value) -> value != null)
                    .map((key, value) -> KeyValue.pair(value.getUserSession(), value.isWon() ? 1.0 : 0.0))
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(GAME_WINDOW_SIZE_MS)))
                    .aggregate(() -> 0.0, (key, value, aggregate) -> aggregate + value,
                            Materialized.<String, Double, WindowStore<Bytes, byte[]>>as(GAME_WINDOWSTORE_NAME + "-player-wins")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Double()));

            // Player attempt totals
            KTable<Windowed<String>, Double> playerAttemptTotals = gameStream
                    .filter((key, value) -> value != null)
                    .map((key, value) -> KeyValue.pair(value.getUserSession(), (double) value.getAttemptsCount()))
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(GAME_WINDOW_SIZE_MS)))
                    .aggregate(() -> 0.0, (key, value, aggregate) -> aggregate + value,
                            Materialized.<String, Double, WindowStore<Bytes, byte[]>>as(GAME_WINDOWSTORE_NAME + "-player-attempts")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Double()));

            // Debug print statements for player streams
            playerGameCounts.toStream()
                    .print(Printed.<Windowed<String>, Long>toSysOut().withLabel("Windowed player game counts"));
            
            playerWinTotals.toStream()
                    .print(Printed.<Windowed<String>, Double>toSysOut().withLabel("Windowed player win totals"));
            
            playerAttemptTotals.toStream()
                    .print(Printed.<Windowed<String>, Double>toSysOut().withLabel("Windowed player attempt totals"));

            return null;
        };
    }
}
