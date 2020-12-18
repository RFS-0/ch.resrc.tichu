package ch.resrc.tichu.capabilities.events;

public class Eventful {

  private final Dispatcher events = new Dispatcher();

  public Subscribable events() {
    return events;
  }

  public Object publish(Object event) {
    events.dispatch(event);
    return event;
  }
}
