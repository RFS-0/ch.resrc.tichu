package ch.resrc.tichu.capabilities.functional;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Reduce {

  public static <T, X> X folded(X x, Collection<T> elements, BiFunction<X, T, X> f) {
    return folded(x, elements.stream(), f);
  }

  public static <T, X> X piped(X initialValue, Collection<T> pipeElements, BiFunction<T, X, X> f) {
    return folded(initialValue, pipeElements, (X x, T t) -> f.apply(t, x));
  }

  public static <T, X> X folded(X x, Stream<T> elements, BiFunction<X, T, X> f) {
    return elements.reduce(x, f, (x1, x2) -> x2);
  }
}
