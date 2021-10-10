package ch.resrc.tichu.use_cases.outbound_ports.eventbus;

import ch.resrc.tichu.capabilities.events.*;

/**
 * Delivers {@code Event}s using the mechanism configured
 * for the application. Typically implemented as an adapter to the
 * event mechanism of the underlying application framework such as
 * Spring or CDI.
 */
public interface EventBus {

    EventBus NO_OP = event -> { /* Ignore it as no-ops do. */};

    void deliver(Event event);
}
