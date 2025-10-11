package com.group18.rotionary.dictionaryanalytics.infrastructure.stream;

import com.group18.rotionary.dictionaryanalytics.domain.entities.QueryEvent;
import com.group18.rotionary.shared.domain.events.TermQueriedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.group18.rotionary.dictionaryanalytics.repository.QueryEventRepository;

import java.util.function.Consumer;

@Configuration
public class QueryEventConsumer {

    private final QueryEventRepository repository;

    public QueryEventConsumer(QueryEventRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Consumer<TermQueriedEvent> recordQuery() {
        return evt -> {
            System.out.println("Received TermQueriedEvent: " + evt.getTermWord() + " (ID: " + evt.getTermId() + ")");
            try {
                QueryEvent qe = new QueryEvent(
                    evt.getTermId(),
                    evt.getTermWord(),
                    QueryEvent.QueryType.valueOf(evt.getQueryType()),
                    evt.getUserSession(),
                    evt.getSearchQuery()
                );
                repository.save(qe);
                System.out.println("Successfully saved QueryEvent for term: " + evt.getTermWord());
            } catch (Exception e) {
                System.err.println("Error saving QueryEvent: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}


