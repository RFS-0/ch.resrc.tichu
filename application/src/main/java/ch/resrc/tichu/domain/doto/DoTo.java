package ch.resrc.tichu.domain.doto;

import static ch.resrc.tichu.capabilities.functional.Reduce.folded;

import ch.resrc.tichu.capabilities.result.Result;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DoTo<T, E> {

  private final Class<E> errorType;
  private Result<T, E> result;

  protected DoTo(Result<T, ? extends E> result, Class<E> errorType) {
    this.result = cast(result);
    this.errorType = errorType;
  }

  protected DoTo(DoTo<T, E> other) {
    this.result = other.result;
    this.errorType = other.errorType;
  }

  public static <T> DoTo<T, Object> of(T value) {
    return new DoTo<>(Result.success(value), Object.class);
  }

  public static <T, E> DoTo<T, E> of(T value, Class<E> errorType) {
    return new DoTo<>(Result.success(value), errorType);
  }

  public static <T, E> DoTo<T, E> of(Result<T, ? extends E> result, Class<E> errorType) {
    return new DoTo<>(result, errorType);
  }

  private static <T, E> Result<T, E> cast(Result<T, ? extends E> result) {
    if (result.isSuccess()) {
      return Result.success(result.value());
    } else {
      return Result.failure(result.errors());
    }
  }

  protected DoTo<T, E> copy() {
    return new DoTo<>(this);
  }

  public <EE> DoTo<T, E> apply(Function<T, Result<T, EE>> f) {
    var theCopy = copy();
    theCopy.result = this.result.flatMap(f, errorType);
    return theCopy;
  }

  public <U, EE extends E> DoTo<T, E> applyAll(BiFunction<T, U, Result<T, EE>> f, Collection<U> elements) {
    return folded(copy(), elements, (d, u) -> d.apply(t -> f.apply(t, u)));
  }

  public Result<T, E> result() {
    return this.result;
  }

  public T value() throws NoSuchElementException {
    return this.result().value();
  }
}
