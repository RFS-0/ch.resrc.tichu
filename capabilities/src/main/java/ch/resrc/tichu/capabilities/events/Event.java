package ch.resrc.tichu.capabilities.events;

import org.apache.commons.lang3.builder.*;

import java.time.*;

public class Event {

    private final Instant occurredOn;

    protected Event() {
        occurredOn = Instant.now();
    }

    public boolean is(Class<? extends Event> eventType) {
        return eventType.isAssignableFrom(this.getClass());
    }

    public Instant occurredOn() {
        return occurredOn;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
