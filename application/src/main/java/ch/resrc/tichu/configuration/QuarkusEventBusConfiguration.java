package ch.resrc.tichu.configuration;

import ch.resrc.tichu.adapters.eventbus.QuarkusEventBus;
import ch.resrc.tichu.eventbus.EventBus;
import javax.enterprise.context.ApplicationScoped;

public class QuarkusEventBusConfiguration {

  io.vertx.core.eventbus.EventBus eventBus;

  public QuarkusEventBusConfiguration(io.vertx.core.eventbus.EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @ApplicationScoped
  public EventBus eventBus() {
    return new QuarkusEventBus(eventBus);
  }

}
