package ch.resrc.tichu.capabilities.result;

import java.util.function.Function;

public interface Mutation<T, E> extends Function<T, Result<T, E>> {

  static <T, E> Mutation<T, E> of(Function<T, Result<T, E>> f) {
    return f::apply;
  }
}
