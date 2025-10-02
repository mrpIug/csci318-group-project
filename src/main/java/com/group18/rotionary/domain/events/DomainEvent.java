package com.group18.rotionary.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Event base class for event-driven architecture
 * All domain events should extend this class
 */
public abstract class DomainEvent {
    
    private final String eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;
    
    protected DomainEvent(String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.eventType = eventType;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    @Override
    public String toString() {
        return "DomainEvent{" +
                "eventId='" + eventId + '\'' +
                ", occurredOn=" + occurredOn +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
