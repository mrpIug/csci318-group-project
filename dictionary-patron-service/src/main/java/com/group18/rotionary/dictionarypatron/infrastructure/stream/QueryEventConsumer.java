package com.group18.rotionary.dictionarypatron.infrastructure.stream;

import com.group18.rotionary.dictionarypatron.domain.entities.QueryEvent;
import com.group18.rotionary.shared.domain.events.TermQueriedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Consumer;

@Configuration
public class QueryEventConsumer {

    interface QueryEventRepository extends JpaRepository<QueryEvent, Long> {}

    private final QueryEventRepository repository;

    public QueryEventConsumer(QueryEventRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Consumer<TermQueriedEvent> recordQuery() {
        return evt -> repository.save(new QueryEvent(
                evt.getTermId(),
                evt.getTermWord(),
                QueryEvent.QueryType.valueOf(evt.getQueryType()),
                evt.getUserSession(),
                evt.getSearchQuery()
        ));
    }
}


