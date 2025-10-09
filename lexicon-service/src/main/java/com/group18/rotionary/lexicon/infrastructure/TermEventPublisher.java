package com.group18.rotionary.lexicon.infrastructure;

import com.group18.rotionary.shared.domain.events.TermQueriedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class TermEventPublisher {

    private final StreamBridge streamBridge;

    public TermEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishTermQueried(TermQueriedEvent event) {
        streamBridge.send("termQueried-out-0", event);
    }
}


