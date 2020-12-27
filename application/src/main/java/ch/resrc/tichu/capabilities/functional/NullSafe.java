package ch.resrc.tichu.capabilities.functional;

import java.util.function.Function;

public class NullSafe {

  public static <T, U> Function<T, U> nullSafe(Function<T, U> f) {
    return (T x) -> x != null ? f.apply(x) : null;
  }

  public static <T, U> U nullSafe(Function<T, U> f, T value) {
    return nullSafe(f).apply(value);
  }

  public static <T> String nullSafeToString(T obj) {
    return nullSafe(Object::toString, obj);
  }
}
