package ch.resrc.tichu.capabilities.result;

import static ch.resrc.tichu.capabilities.functional.PersistentCollections.addedTo;
import static ch.resrc.tichu.capabilities.functional.Reduce.folded;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the result of an operation that might fail with one or more errors. Holds the result object of the operation if the
 * operation was successful. Holds all errors if the operation has failed.
 * <p>
 * Accumulates the errors of multiple results.
 *
 * @param <T> the result object type of the operation
 * @param <E> the type of the errors
 */
public abstract class Result<T, E> {

  public static <T, E> Result<T, E> success(T value) {
    return new Success<>(value);
  }

  public static <T, E> Result<T, E> failure(E error) {
    return new Failure<>(List.of(error));
  }

  public static <T, E> Result<T, E> failure(Collection<? extends E> errors) {
    return new Failure<>(List.copyOf(errors));
  }

  public static <E> Result<Void, E> voidSuccess() {
    return new VoidSuccess<>();
  }

  public static <E> Result<Void, E> voidFailure(E error) {
    return new VoidFailure<>(List.of(error));
  }

  public static <E> Result<Void, E> voidFailure(Collection<? extends E> errors) {
    return new VoidFailure<>(List.copyOf(errors));
  }

  public static <T, E> Result<T, E> empty() {
    return new EmptySuccess<>();
  }

  @SafeVarargs
  public static <EE> Combination<EE> combined(Result<?, EE>... results) {
    return Result.combined(List.of(results));
  }

  public static <EE> Combination<EE> combined(List<? extends Result<?, EE>> results) {
    return new Combination<>(results);
  }

  /**
   * Applies the given mapping function to each element of the supplied collection. If all elements map to a Success, a Success result
   * is returned with the list of the mapped values. If any of the elements maps to a Failure, a Failure is returned that contains the
   * errors of all elements that mapped to a Failure.
   *
   * @param elements the items to be mapped
   * @param mapping  applied to each element
   * @param <T>      the type of the source elements
   * @param <U>      the type of the target elements
   * @param <E>      the error type of the mapping
   * @return a {@code Result}, as explained
   */
  public static <T, U, E> Result<List<U>, E> ofMapping(Collection<T> elements, Function<T, Result<U, E>> mapping) {
    List<Result<U, E>> results = elements.stream()
      .map(mapping)
      .collect(toList());

    return Result.combined(results)
      .yield(() -> valuesOf(results));
  }

  /**
   * Returns a {@link Collector} that combines a stream of results into a VoidResult. The VoidResult is a VoidSuccess if all results of
   * the stream are Successes. The VoidResult is a VoidFailure if any of the results in the stream is a Failure. The errors of the
   * VoidFailure are the union of all Failure results in the stream.
   *
   * @param <E> the error type of the results in the stream
   * @return a {@link Collector}, as explained
   */
  public static <E> Collector<Result<?, E>, ?, Result<Void, E>> combinedToVoidResult() {
    return Collectors.collectingAndThen(toList(), x -> Result.combined(x).yieldVoid());
  }


  /**
   * Returns a {@link Collector} that combines a stream of results into a result of a list. The list contains the values of all results
   * in the stream.
   * <p>
   * The result is a Success if all results in the stream are Successes. The result is a Failure if any of the results in the stream is
   * a Failure. The errors of the Failure are the union of all Failure results in the stream.
   *
   * @param <T> the value type of the results in the stream
   * @param <E> the error type of the results in the stream
   * @return a {@link Collector}, as explained
   */
  public static <T, E> Collector<Result<T, E>, ?, Result<List<T>, E>> combinedToListResult() {
    return Collectors.collectingAndThen(
      toList(),
      (List<Result<T, E>> x) -> Result.combined(x).yield(() -> valuesOf(x))
    );
  }

  public static <T, E> Result<List<T>, E> combinedToListResult(Collection<Result<T, E>> results) {
    return results.stream().collect(combinedToListResult());
  }

  /**
   * Tells whether this object is a success result. Note that an empty result is also a success result.
   *
   * @return true, if this object is a Success. False otherwise.
   */
  public abstract boolean isSuccess();

  /**
   * Tells whether this object is a failure result. A failure result contains at least one error.
   *
   * @return true, if this object is a Failure. False otherwise.
   */
  public abstract boolean isFailure();

  /**
   * Tells whether this object is an empty result. Note that an empty result is also a success result.
   *
   * @return true, if this object is empty. False otherwise.
   */
  public abstract boolean isEmpty();

  /**
   * @return the result object if the result is a Success
   * @throws NoSuchElementException if this object is a Failure
   */
  public abstract T value() throws NoSuchElementException;

  public <U> U value(Function<? super T, U> f) throws NoSuchElementException {

    return f.apply(value());
  }

  /**
   * @return all errors if the result is a Failure. An empty list if the result is a Success.
   */
  public abstract List<E> errors() throws NoSuchElementException;

  /**
   * Maps the result object to another object if the result is a Success. The new result is a Success, too. If this result is a
   * Failure, the mapping function is not invoked.
   *
   * @param f   the mapping function
   * @param <U> the type of the mapping target
   * @return a new instance for the mapped object
   */
  public abstract <U> Result<U, E> map(Function<T, U> f);

  public abstract <U> Result<U, E> flatMap(Function<T, Result<U, E>> f);

  public <U, EE> Result<U, E> flatMap(Function<T, Result<U, EE>> f, Function<? super EE, E> fe) {
    if (isEmpty()) {
      return Result.empty();
    } else if (isSuccess()) {
      return f.apply(value()).mapErrors(fe);
    } else {
      return Result.failure(errors());
    }
  }

  public <U, EE, EEE> Result<U, EEE> flatMap(Function<T, Result<U, EE>> f, Class<EEE> errorType) {
    if (isEmpty()) {
      return Result.empty();
    } else if (isSuccess()) {
      return f.apply(value()).castErrors(errorType);
    } else {
      return Result.failure(this.castErrors(errorType).errors());
    }
  }

  /**
   * Maps a Collection of Results to a list of the values of the Results.
   *
   * @param results the results to map
   * @param <T>     the value type of the results
   * @param <E>     the error type of the results
   * @return the list of values
   * @throws NoSuchElementException if any of the results is a Failure
   */
  public static <T, E> List<T> valuesOf(Collection<Result<T, E>> results) throws NoSuchElementException {
    return results.stream().map(Result::value).collect(toList());
  }

  /**
   * Maps a collection of Results to a list of their values but only for the success results. The result list may therefore be smaller
   * than the input collection.
   *
   * @param <T> the value type of the results
   * @param <E> the error type of the results
   * @return the list of values
   */
  public static <T, E> List<T> successValuesOf(Collection<Result<T, E>> results) {
    return results.stream().filter(Result::isSuccess).map(Result::value).collect(toList());
  }

  /**
   * Syntactic sugar. Alias for {@link #flatMap(Function)}.
   *
   * @param f the mapping function to apply
   * @return the mapping result or this object depending on whether this object is a Success or Failure
   */
  public <U> Result<U, E> andThen(Function<T, Result<U, E>> f) {
    return this.flatMap(f);
  }

  /**
   * Maps the errors to a different error representation. Does nothing if this result is a Success.
   *
   * @param f    maps a single error to the new error representation. Invoked for each error.
   * @param <EE> the type of the new error representation
   * @return a new instance of this object with all errors mapped as prescribed
   */
  public abstract <EE> Result<T, EE> mapErrors(Function<? super E, EE> f);

  @SafeVarargs
  public static <T, E> Result<T, E> modifyErrors(Result<T, E> result, Function<E, E>... errorModifiers) {
    return folded(result, Stream.of(errorModifiers), Result::mapErrors);
  }

  public Result<T, E> mapEmptyToError(Supplier<E> error) {
    if (this.isEmpty()) {
      return Result.failure(error.get());
    } else {
      return this;
    }
  }

  /**
   * Casts all errors to the given type if this object is a Failure.
   *
   * @param targetType the type to cast to
   * @param <EE>       the target type
   * @return a new result with the given error type.
   */
  public <EE> Result<T, EE> castErrors(Class<EE> targetType) {
    return mapErrors(targetType::cast);
  }

  /**
   * Applies the given error handling to this object. The error handling may have side effects an can throw exceptions as an error
   * handling strategy.
   *
   * @param errorHandling the error handling to apply
   * @param <EE>          the error type of the error handling result
   * @return the result produced by the error handling
   * @throws RuntimeException if the error handling decides to throw on an error
   */
  public <EE> Result<T, EE> handleErrors(ErrorHandling<T, E, EE> errorHandling) throws RuntimeException {
    return errorHandling.apply(this);
  }

  /**
   * Returns the result value if the result is a Success. Throws the specified exception if it is a Failure.
   *
   * @param errorMap converts any errors to the exception that should be thrown if the result is a Failure.
   * @return the result value, if the result is a Success.
   * @throws RuntimeException (as defined by the errorMap) if the result is a Failure.
   */
  public abstract T getOrThrow(Function<List<E>, ? extends RuntimeException> errorMap) throws RuntimeException;


  /**
   * Throws the specified exception if the result is a Failure. Does nothing if the result is a Success.
   *
   * @param errorMap converts the errors to the exception that should be thrown if this object is a Failure
   */
  public abstract Result<T, E> ifFailureThrow(Function<List<E>, ? extends RuntimeException> errorMap);

  /**
   * Recovers an alternative Success value, if this object is a Failure. Returns the provided object as a Success result if this object
   * is a Failure. Has no effect if this object is already a Success.
   *
   * @param alternativeValue the alternative value to use if this object is a Failure
   * @return the possibly alternative Success, as explained
   */
  public abstract <U extends T> Result<T, E> recover(U alternativeValue);

  public abstract <U extends T> Result<T, E> recoverEmpty(Supplier<U> alternative);

  public abstract Result<T, E> ifEmptySwitch(Supplier<Result<T, E>> alternative);

  public abstract Result<T, E> failIfSuccess(E error);

  /**
   * Returns a new Success with the specified value if this object is a Success. Returns a Failure with the errors of this object if
   * this object is a Failure.
   *
   * @param value supplies the value of the new Success
   * @param <U>   the type of the new Success value
   * @return the new Success or Failure as explained
   */
  public <U> Result<U, E> yield(Supplier<U> value) {
    return yieldResult(() -> Result.success(value.get()));
  }

  /**
   * Returns a new VoidSuccess if this object is a Success. Returns a VoidFailure with the errors of this object if this object is a
   * Failure.
   *
   * @return the new VoidSuccess or VoidFailure as explained
   */
  public Result<Void, E> yieldVoidSuccess() {
    return yieldResult(Result::voidSuccess);
  }

  /**
   * Returns the specified result if this object is a Success. Returns a Failure with the errors of this object if this object is a
   * Failure.
   *
   * @param newResult supplies the new result
   * @param <U>       the value type of the new result
   * @return the new result or Failure as explained
   */
  public abstract <U> Result<U, E> yieldResult(Supplier<Result<U, E>> newResult);

  /**
   * Executes an effect if this object is a Success. Does nothing if this object is a Failure
   *
   * @param effect the effect to execute
   * @return this object
   */
  public abstract Result<T, E> ifSuccess(Runnable effect);

  /**
   * Executes an effect on the value of this object if this object is a Success. Does nothing if this object is a Failure
   *
   * @param effect the effect to execute
   * @return this object
   */
  public abstract Result<T, E> ifSuccess(Consumer<T> effect);

  public Result<T, E> ifFailure(Consumer<List<E>> effect) {
    if (isFailure()) {
      effect.accept(errors());
    }

    return this;
  }

  /**
   * Executes an error effect for each error of this object if this object is a Failure. Does nothing if this object is a Success.
   *
   * @param errorEffect the effect to execute for each error
   * @return this object
   */
  public abstract Result<T, E> forEachError(Consumer<E> errorEffect);

  /**
   * Represents the result of a successful operation.
   */
  static class Success<T, E> extends Result<T, E> {

    private final T value;

    public Success(T value) {
      this.value = value;
    }

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public boolean isFailure() {
      return false;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public T value() {
      return this.value;
    }

    @Override
    public List<E> errors() {

      return List.of();
    }

    @Override
    public <U> Result<U, E> map(Function<T, U> f) {

      return Result.success(f.apply(this.value));
    }

    @Override
    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> f) {

      return f.apply(this.value);
    }

    @Override
    public <EE> Result<T, EE> mapErrors(Function<? super E, EE> f) {
      return Result.success(this.value);
    }

    @Override
    public T getOrThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      return this.value;
    }


    @Override
    public Result<T, E> ifFailureThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      return this;
    }

    @Override
    public <U extends T> Result<T, E> recover(U alternativeValue) {
      return this;
    }

    @Override
    public <U extends T> Result<T, E> recoverEmpty(Supplier<U> alternative) {
      return this;
    }

    @Override
    public Result<T, E> ifEmptySwitch(Supplier<Result<T, E>> alternative) {
      return this;
    }

    @Override
    public Result<T, E> failIfSuccess(E error) {

      return Result.failure(error);
    }

    @Override
    public <U> Result<U, E> yieldResult(Supplier<Result<U, E>> newResult) {
      return newResult.get();
    }

    @Override
    public Result<T, E> ifSuccess(Runnable effect) {

      effect.run();
      return this;
    }

    @Override
    public Result<T, E> ifSuccess(Consumer<T> effect) {

      effect.accept(value);
      return this;
    }

    @Override
    public Result<T, E> forEachError(Consumer<E> errorEffect) {
      return this;
    }
  }

  /**
   * Represents the result of a failed operation.
   */
  static class Failure<T, E> extends Result<T, E> {


    private final List<E> errors;

    public Failure(List<E> errors) {
      this.errors = List.copyOf(errors);
    }

    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public boolean isFailure() {
      return true;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public T value() {
      throw new NoSuchElementException("This is a Failure result. There is no result value.");
    }

    @Override
    public List<E> errors() {
      return List.copyOf(this.errors);
    }

    @Override
    public <U> Result<U, E> map(Function<T, U> f) {
      return new Failure<>(this.errors);
    }

    @Override
    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> f) {
      return new Failure<>(this.errors);
    }

    @Override
    public <EE> Result<T, EE> mapErrors(Function<? super E, EE> f) {

      List<EE> mappedErrors = this.errors.stream().map(f).collect(toList());
      return new Failure<>(mappedErrors);
    }

    @Override
    public T getOrThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      throw errorMap.apply(this.errors);
    }

    @Override
    public Result<T, E> ifFailureThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      throw errorMap.apply(this.errors);
    }

    @Override
    public <U extends T> Result<T, E> recover(U alternativeValue) {
      return Result.success(alternativeValue);
    }

    @Override
    public <U extends T> Result<T, E> recoverEmpty(Supplier<U> alternative) {
      return this;
    }

    @Override
    public Result<T, E> ifEmptySwitch(Supplier<Result<T, E>> alternative) {
      return this;
    }

    @Override
    public Result<T, E> failIfSuccess(E error) {
      return this;
    }

    @Override
    public <U> Result<U, E> yieldResult(Supplier<Result<U, E>> newResult) {
      return new Failure<>(this.errors);
    }

    @Override
    public Result<T, E> ifSuccess(Runnable effect) {
      return this;
    }

    @Override
    public Result<T, E> ifSuccess(Consumer<T> effect) {
      return this;
    }

    @Override
    public Result<T, E> forEachError(Consumer<E> errorEffect) {
      this.errors.forEach(errorEffect);
      return this;
    }
  }

  static class EmptySuccess<T, E> extends Result<T, E> {

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public boolean isFailure() {
      return false;
    }

    @Override
    public T value() throws NoSuchElementException {

      throw new NoSuchElementException("This is an empty result.");
    }

    @Override
    public List<E> errors() throws NoSuchElementException {
      return List.of();
    }

    @Override
    public <U> Result<U, E> map(Function<T, U> f) {

      return Result.empty();
    }

    @Override
    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> f) {
      return Result.empty();
    }

    @Override
    public <EE> Result<T, EE> mapErrors(Function<? super E, EE> f) {
      return Result.empty();
    }

    @Override
    public T getOrThrow(Function<List<E>, ? extends RuntimeException> errorMap) throws RuntimeException {
      throw new NoSuchElementException("This is an empty result.");
    }

    @Override
    public Result<T, E> ifFailureThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      return Result.empty();
    }

    @Override
    public <U extends T> Result<T, E> recover(U alternativeValue) {
      return Result.success(alternativeValue);
    }

    @Override
    public <U extends T> Result<T, E> recoverEmpty(Supplier<U> alternative) {
      return Result.success(alternative.get());
    }

    @Override
    public Result<T, E> ifEmptySwitch(Supplier<Result<T, E>> alternative) {
      return alternative.get().map(x -> x);
    }

    @Override
    public Result<T, E> failIfSuccess(E error) {
      return Result.failure(error);
    }

    @Override
    public <U> Result<U, E> yieldResult(Supplier<Result<U, E>> newResult) {
      return newResult.get();
    }

    @Override
    public Result<T, E> ifSuccess(Runnable effect) {
      effect.run();
      return this;
    }

    @Override
    public Result<T, E> ifSuccess(Consumer<T> effect) {
      throw new NoSuchElementException("This is an empty result.");
    }

    @Override
    public Result<T, E> forEachError(Consumer<E> errorEffect) {
      return this;
    }
  }

  /**
   * Represents the result of a successful void operation.
   */
  static class VoidSuccess<E> extends Result<Void, E> {

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public boolean isFailure() {
      return false;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Void value() {
      throw new NoSuchElementException("This is a VoidSuccess result. There is no value.");
    }

    @Override
    public List<E> errors() {
      return List.of();
    }

    @Override
    public <U> Result<U, E> map(Function<Void, U> f) {
      throw unsupportedOperation("VoidSuccess");
    }

    @Override
    public <U> Result<U, E> flatMap(Function<Void, Result<U, E>> f) {
      throw unsupportedOperation("VoidSuccess");
    }

    @Override
    public <EE> VoidSuccess<EE> mapErrors(Function<? super E, EE> f) {
      return new VoidSuccess<>();
    }

    @Override
    public Void getOrThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      throw unsupportedOperation("VoidSuccess");
    }


    @Override
    public VoidSuccess<E> ifFailureThrow(Function<List<E>, ? extends RuntimeException> errorMap) {
      return this;
    }

    @Override
    public VoidSuccess<E> recover(Void alternativeValue) {

      throw unsupportedOperation("VoidSuccess");
    }

    @Override
    public <U extends Void> Result<Void, E> recoverEmpty(Supplier<U> alternative) {
      return this;
    }

    @Override
    public Result<Void, E> ifEmptySwitch(Supplier<Result<Void, E>> alternative) {
      return this;
    }

    @Override
    public Result<Void, E> failIfSuccess(E error) {

      return Result.voidFailure(error);
    }

    @Override
    public <U> Result<U, E> yieldResult(Supplier<Result<U, E>> newResult) {
      return newResult.get();
    }

    @Override
    public VoidSuccess<E> ifSuccess(Runnable effect) {

      effect.run();
      return this;
    }

    @Override
    public VoidSuccess<E> ifSuccess(Consumer<Void> effect) {

      throw unsupportedOperation("VoidSuccess");
    }

    @Override
    public VoidSuccess<E> forEachError(Consumer<E> errorEffect) {
      return this;
    }
  }


  /**
   * Represents the result of a failed void operation.
   */
  static class VoidFailure<E> extends Result<Void, E> {

    private final Failure<Void, E> failure;

    public VoidFailure(List<E> errors) {

      this.failure = new Failure<>(errors);
    }

    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public boolean isFailure() {
      return true;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Void value() throws NoSuchElementException {
      return failure.value();
    }

    @Override
    public List<E> errors() throws NoSuchElementException {
      return failure.errors();
    }

    @Override
    public VoidFailure<E> ifFailureThrow(Function<List<E>, ? extends RuntimeException> errorMap) {

      failure.ifFailureThrow(errorMap);
      return this;
    }

    @Override
    public <U> Result<U, E> yieldResult(Supplier<Result<U, E>> newResult) {
      return failure.yieldResult(newResult);
    }

    @Override
    public VoidFailure<E> ifSuccess(Runnable effect) {
      return this;
    }

    @Override
    public VoidFailure<E> ifSuccess(Consumer<Void> effect) {
      return this;
    }

    @Override
    public VoidFailure<E> forEachError(Consumer<E> errorEffect) {

      failure.forEachError(errorEffect);
      return this;
    }

    @Override
    public <U> Result<U, E> map(Function<Void, U> f) {

      throw unsupportedOperation("VoidFailure");
    }

    @Override
    public <U> Result<U, E> flatMap(Function<Void, Result<U, E>> f) {

      throw unsupportedOperation("VoidFailure");
    }

    @Override
    public <EE> VoidFailure<EE> mapErrors(Function<? super E, EE> f) {

      List<EE> mappedErrors = failure.mapErrors(f).errors();
      return new VoidFailure<>(mappedErrors);
    }

    @Override
    public Void getOrThrow(Function<List<E>, ? extends RuntimeException> errorMap) {

      throw unsupportedOperation("VoidFailure");
    }

    @Override
    public Result<Void, E> recover(Void alternativeValue) {

      throw unsupportedOperation("VoidFailure");
    }

    @Override
    public <U extends Void> Result<Void, E> recoverEmpty(Supplier<U> alternative) {
      return this;
    }

    @Override
    public Result<Void, E> ifEmptySwitch(Supplier<Result<Void, E>> alternative) {
      return this;
    }

    @Override
    public Result<Void, E> failIfSuccess(E error) {
      return this;
    }
  }

  public static class Combination<EE> {

    private final List<Result<?, EE>> results;

    Combination(List<? extends Result<?, EE>> results) {
      this.results = List.copyOf(results);
    }


    public Combination<EE> combined(List<? extends Result<?, EE>> otherResults) {
      return new Combination<>(addedTo(this.results, otherResults));
    }

    public Combination<EE> combined(Result<?, EE> otherResult) {
      return combined(List.of(otherResult));
    }

    /**
     * Maps this combination to a new result. Returns a Success if all combined results are Successes. Returns a Failure if any of the
     * combined results is a Failure. The returned Failure contains the union of all errors of all Failures in the combined results.
     * <p>
     * The given supplier produces the success value. It is only invoked if all combined results are Successes. The supplier must not
     * throw an exception if this combination is a Success.
     * </p>
     *
     * @param successValue supplies the result value if this combination is a Success
     * @param <U>          the type of the result value
     * @return a {@link Result} as explained.
     */
    public <U> Result<U, EE> yield(Supplier<U> successValue) {
      return this.yieldResult(() -> Result.success(successValue.get()));
    }

    /**
     * Maps this combination to a VoidSuccess if all combined results are successes. Returns a Failure if any of the combined results
     * is a Failure. The returned Failure contains the union of all errors of all Failures in the combined results.
     *
     * @return a {@link Result} as explained
     */
    public Result<Void, EE> yieldVoid() {
      return this.yieldResult(Result::voidSuccess);
    }

    /**
     * Maps this combination to a new result. Returns a Success if all combined results are Successes. Returns a Failure if any of the
     * combined results is a Failure. The returned Failure contains the union of all errors of all Failures in the combined results.
     * <p>
     * The given supplier produces the returned new result if this combination is a Success. It is only invoked if this combination is
     * a Success. The supplier must not throw an exception if this combination is a Success. However, the supplied result may be a
     * Failure.
     * </p>
     * This function is useful, for example, for chaining validations, where a downstream validation can only be carried out if a
     * combination of upstream validations succeeds. For instance, the downstream validation could be a cross validation of object
     * attributes. The cross validation can only be done if the individual attributes are syntactically valid.
     *
     * @param newResult supplies the new result if this combination is a Success
     * @param <U>       the type of the result value
     * @return a {@link Result} as explained.
     */
    public <U> Result<U, EE> yieldResult(Supplier<Result<U, EE>> newResult) {
      List<EE> errors = results.stream()
        .filter(Result::isFailure)
        .flatMap(x -> x.errors().stream())
        .collect(toList());

      if (errors.isEmpty()) {
        try {
          return newResult.get();
        } catch (Exception bad) {
          throw new IllegalStateException(
            "Failed to map to a new result although this combination is a Success.", bad
          );
        }
      } else {
        return new Failure<>(errors);
      }
    }
  }

  private static UnsupportedOperationException unsupportedOperation(String resultType) {
    return new UnsupportedOperationException(format(
      "This is a %s result. It does not make sense to call this operation for this result type.",
      resultType));
  }

  public static class Functions {

    public static <T, E> Consumer<Result<T, E>> ifSuccess(Consumer<T> effect) {
      return (Result<T, E> result) -> result.ifSuccess(effect);
    }

    public static <T, E> Consumer<Result<T, E>> ifSuccess(Runnable effect) {
      return (Result<T, E> result) -> result.ifSuccess(effect);
    }

    public static <T, E> Function<Result<T, E>, Result<T, E>> flatMapResult(Function<T, Result<T, E>> f) {
      return (Result<T, E> r) -> r.flatMap(f);
    }
  }
}
