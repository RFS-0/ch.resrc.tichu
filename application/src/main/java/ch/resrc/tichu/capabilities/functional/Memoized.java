package ch.resrc.tichu.capabilities.functional;

import io.vavr.Function1;
import java.util.function.Function;

public class Memoized {

  public static <T, U> Function<T, U> memoized(Function<T, U> f) {
    return Function1.of(f::apply).memoized();
  }
}
