package ch.resrc.tichu.adapters.eventbus;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.eventbus.EventBus;


public class QuarkusEventBus implements EventBus {

  private final io.vertx.core.eventbus.EventBus eventBus;

  public QuarkusEventBus(io.vertx.core.eventbus.EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void publish(String address, Event event) {
    eventBus.publish(address, event);
  }
}
