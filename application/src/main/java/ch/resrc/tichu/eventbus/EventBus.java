package ch.resrc.tichu.eventbus;

import ch.resrc.tichu.capabilities.events.Event;

/**
 * Publishes {@code Event}s using the mechanism configured for the application. Typically implemented as an adapter to the event
 * mechanism of the underlying application framework such as Spring or CDI.
 */
public interface EventBus {

  EventBus NO_OP = (address, event) -> { /* Ignore it as no-ops do. */};

  void publish(String address, Event event);
}
