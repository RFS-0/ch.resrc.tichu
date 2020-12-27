package ch.resrc.tichu.ports.outbound.authorization;


import ch.resrc.tichu.capabilities.events.Event;

/**
 * Signals that some data item was accessed for reading. Does not mean that the data was mutated.
 *
 * @param <T> the type of the accessed data item.
 */
public class DataAccessed<T> extends Event {

  private final T accessedData;

  public DataAccessed(T accessedData) {

    this.accessedData = accessedData;
  }

  public static <T> DataAccessed<T> of(T accessedItem) {

    return new DataAccessed<>(accessedItem);
  }

  public T accessedData() {
    return accessedData;
  }

}
