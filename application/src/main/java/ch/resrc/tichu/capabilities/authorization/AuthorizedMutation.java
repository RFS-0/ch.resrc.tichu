package ch.resrc.tichu.capabilities.authorization;

import static ch.resrc.tichu.capabilities.authorization.guarded.GuardedFunctions.guarded;
import static ch.resrc.tichu.capabilities.functional.Reduce.folded;

import ch.resrc.tichu.capabilities.authorization.guarded.Guarded;
import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.capabilities.errorhandling.HavingDiagnosis;
import ch.resrc.tichu.capabilities.result.ErrorHandling;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.ports.outbound.authorization.MutationFailed;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Performs mutations of functional objects such that the mutation can only succeed if the client is authorized to do the mutation. No
 * information about an attempted mutation can escape this object unless the mutation has been authorized successfully.
 * <p>
 * This object first attempts the mutation and then authorizes the resulting change events present in the mutated object's change log.
 * This approach of authorizing after the fact requires that the mutated object is a functional object which is technically immutable
 * so that attempting the mutation has no visible side effects. Moreover, the mutation functions that this object applies must be pure
 * functions without any side effects. If you ignore these requirements you might compromise data access security.
 * <p>
 * This approach has the great advantage that the mutation events to be authorized need not be known in advance.
 * <p>
 * If the mutation signals a business error, this object does not report the business error unless the underlying mutation attempt was
 * authorized. If the mutation was not authorized, the business error is suppressed and an authorization error is returned instead. So
 * it is impossible to gain information about the mutation through the error message if the client was not allowed to do the mutation
 * in the first place. This approach requires that the business errors produced by the mutated object carry the change event that
 * describes the attempted mutation.
 * </p>
 *
 * @param <T> the type of the object that gets mutated.
 */
public class AuthorizedMutation<T extends ChangeLogging> {

  private final Result<Guarded<T>, HavingDiagnosis> lastMutationResult;
  protected final AccessAuthorization auth;

  private AuthorizedMutation(Guarded<T> toBeMutated, AccessAuthorization auth) {
    this.lastMutationResult = Result.success(toBeMutated);
    this.auth = auth;
  }

  protected AuthorizedMutation(AccessAuthorization auth) {
    this.lastMutationResult = Result.empty();
    this.auth = auth;
  }

  protected AuthorizedMutation(AuthorizedMutation<T> other, Result<Guarded<T>, HavingDiagnosis> mutationResult) {
    this.auth = other.auth;
    this.lastMutationResult = mutationResult;
  }

  public static <T extends ChangeLogging> AuthorizedMutation<T> of(T obj, AccessAuthorization auth) {
    return new AuthorizedMutation<>(Guarded.ofContinued(obj), auth);
  }

  /**
   * Applies the given mutation function to the mutated object. Authorizes the mutation.
   *
   * @param mutation the mutation to apply to the mutated object. Must be a pure function.
   * @param <E>      the error type produced by the mutation
   * @return this object for chained calls
   */
  public <E extends HavingDiagnosis> AuthorizedMutation<T> mutated(Function<T, Result<T, E>> mutation) {
    if (this.lastMutationResult.isFailure()) {
      return this;
    }

    Result<Guarded<T>, HavingDiagnosis> mutated =
      this.lastMutationResult.flatMap(guarded(mutation), HavingDiagnosis.class)
        .handleErrors(authorizeMutationErrors())
        .flatMap((Guarded<T> x) -> x.authorized(auth), HavingDiagnosis.class);

    return new AuthorizedMutation<>(this, mutated);
  }

  public <U, E extends HavingDiagnosis> AuthorizedMutation<T> applyAll(BiFunction<T, U, Result<T, E>> f, Collection<U> elements) {
    return folded(this, elements, (d, u) -> d.mutated(t -> f.apply(t, u)));
  }

  /**
   * Returns the result of all mutations applied through this object. If the result is a failure, it contains the errors of the first
   * mutation that failed. These are the business errors raised by the failing mutation function if the mutation was authorized. If the
   * mutation failed with business errors and was unauthorized, the authorization errors are returned instead. The business errors are
   * suppressed in that case.
   *
   * @return the result of all mutations applied through this object.
   */
  public Result<T, HavingDiagnosis> result() {
    return this.lastMutationResult.map(Guarded::tryYield);
  }

  protected ErrorHandling<Guarded<T>, HavingDiagnosis, HavingDiagnosis> authorizeMutationErrors() {
    return (Result<Guarded<T>, HavingDiagnosis> mutationResult) ->
      mutationResult.errors()
        .stream()
        .map(this::authorizedMutationError)
        .collect(Result.combinedToVoidResult())
        .yieldResult(() -> mutationResult);
  }

  private Result<Void, HavingDiagnosis> authorizedMutationError(HavingDiagnosis mutationError) {
    return auth.authorize(MutationFailed.of(mutationError))
      .castErrors(HavingDiagnosis.class)
      .failIfSuccess(mutationError);
  }
}
