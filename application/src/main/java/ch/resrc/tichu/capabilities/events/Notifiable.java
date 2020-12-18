package ch.resrc.tichu.capabilities.events;

public interface Notifiable {

  Object on(Object event);

  default <T extends Eventful> T observe(T observable) {
    observable.events().subscribe(this);
    return observable;
  }
}
