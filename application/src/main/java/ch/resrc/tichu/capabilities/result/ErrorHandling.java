package ch.resrc.tichu.capabilities.result;

/**
 * Transforms a {@link Result} to a new {@code Result} with a possibly different error content. The transform may have side effects. In
 * particular it may throw exceptions depending on the error content of the result that gets transformed.
 * <p>
 * Only acts on Failure results. If the transformed result is a Success, the transform does not change the value content. The transform
 * may yield a result with a new nominal error type.
 * </p>
 * <p>
 * An error transform is useful to encapsulate error handling patterns that go through a sequence of {@code Result} operations.
 * </p>
 *
 * @param <T>  the value type of the {@code Result} that gets transformed
 * @param <E>  the error type of the {@code Result} that gets transformed
 * @param <EE> the error type of the transformation result
 */
public interface ErrorHandling<T, E, EE> {

  Result<T, EE> apply(Result<T, E> result) throws RuntimeException;
}
