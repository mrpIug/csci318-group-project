package com.group18.rotionary.rotlegame.infrastructure;

import com.group18.rotionary.shared.domain.events.GameCompletedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class GameEventPublisher {

    private final StreamBridge streamBridge;

    public GameEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishGameCompleted(GameCompletedEvent event) {
        streamBridge.send("gameCompleted-out-0", event);
    }
}
