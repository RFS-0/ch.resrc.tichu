package ch.resrc.tichu.capabilities.functional;

@FunctionalInterface
public interface Consumer3<T, U, V> {

  void accept(T t, U u, V v);
}
