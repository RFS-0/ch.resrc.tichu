package ch.resrc.tichu.capabilities.events;

public interface Notifiable {

    void on(Object notification);

    default <T extends Eventful> T observe(T observable) {
        observable.events().subscribe(this);
        return observable;
    }
}
