package ch.resrc.tichu.capabilities.events;

/**
 * Trait interface that adds the capability to forward events of other {@link Eventful} objects to the implementor's subscribers.
 */
public interface EventForwarding extends Notifiable {

  Object publish(Object anEvent);

  default Object on(Object anEvent) {
    publish(anEvent);
    return anEvent;
  }
}
