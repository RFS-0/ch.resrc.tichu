package ch.resrc.tichu.capabilities.errorhandling;

import static ch.resrc.tichu.usecases.errors.UseCaseProblem.INVALID_INPUT_DETECTED;
import static io.vavr.Predicates.instanceOf;

import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.errorhandling.faults.Fault;
import ch.resrc.tichu.capabilities.errorhandling.faults.OurFault;
import ch.resrc.tichu.capabilities.result.ErrorHandling;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.ports.outbound.authorization.AuthorizationProblem;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Blame {

  /**
   * Returns a predicate that tells whether a given event signals that a {@link ClientFault} has occurred. Useful for matching error
   * events and invoking an appropriate error reaction.
   *
   * @return the predicate, as explained
   */
  public static Predicate<ClientFault> isClientFault() {
    return instanceOf(ClientFault.class);
  }

  public static <T, E> Function<Optional<T>, Result<T, E>> orElseFailure(Supplier<E> problem) {
    return (Optional<T> optional) -> optional.map(Result::<T, E>success)
      .orElse(Result.failure(problem.get()));
  }

  public static <X extends RuntimeException, Y extends X> Consumer<X> throwUnless(Class<Y> noThrow) throws X {
    return (X error) -> {
      if (noThrow.isAssignableFrom(error.getClass())) {
        return;
      }

      throw error;
    };
  }

  /**
   * Returns an {@link ErrorHandling} that maps the given problems to {@link ClientFault}s. The transform throws an {@link OurFault}
   * for the first problem that is not contained in the given problem set, because other problems are unexpected and signal a system
   * malfunction.
   * <p>
   * This transform represents our pattern for identifying client errors and treating them separately from all errors caused by a
   * system malfunction.
   * </p>
   *
   * @param problems the problems that should be interpreted as {@code ClientFault}s
   * @param <T>      the value type of the result that gets transformed
   * @param <E>      the error type of the result that gets transformed
   * @return an {@link ErrorHandling}, as explained
   */
  public static <T, E extends HavingDiagnosis> ErrorHandling<T, E, ClientFault> expectAsClientFaults(Problem... problems) {
    return (Result<T, E> toBeTransformed) -> toBeTransformed.mapErrors(blameClientFor(problems))
      .forEachError(throwUnless(ClientFault.class))
      .castErrors(ClientFault.class);
  }

  public static <T, E extends HavingDiagnosis> ErrorHandling<T, E, ClientFault> expectClientAccessDenied() {
    return expectAsClientFaults(AuthorizationProblem.ACCESS_DENIED);
  }


  /**
   * Returns an {@link ErrorHandling} that throws an exception if the result to be handled is a Failure. In that case the exception
   * representation of the first error is thrown .
   * <p>
   * This error handling represents our error handling pattern if an operation should not produce any errors unless the system
   * malfunctions.
   * </p>
   *
   * @param <T> the value type of the result that gets handled
   * @param <E> the error type of the result that gets handled
   * @return an error handling, as explained.
   */
  public static <T, E extends HavingExceptionRepresentation> ErrorHandling<T, E, E> expectNoProblems() {
    return (Result<T, E> toBeHandled) -> toBeHandled.forEachError(x -> {
      throw x.asException();
    });
  }

  public static ClientFault asClientFault(ValidationError validationError) {
    return ClientFault.of(
      ProblemDiagnosis.of(INVALID_INPUT_DETECTED)
        .withContext("validationMessage", validationError.errorMessage())
    );
  }

  public static ClientFault asClientFault(InvalidInputDetected bad) {
    return ClientFault.of(ProblemDiagnosis.of(INVALID_INPUT_DETECTED)
      .withContext("validationMessage", bad.getMessage())
      .withCause(bad));

  }

  public static <E extends HavingDiagnosis> Function<E, Fault> blameClientFor(Problem... blamableProblems) {

    return blameClientFor(List.of(blamableProblems));
  }

  public static <E extends HavingDiagnosis> Function<E, Fault> blameClientFor(Collection<Problem> blamableProblems) {

    return blameFor(blamableProblems, ClientFault::of);
  }

  private static <E extends HavingDiagnosis> Function<E, Fault> blameFor(
    Collection<Problem> blamableProblems,
    Function<ProblemDiagnosis, Fault> blame
  ) {
    return (E error) -> {
      Problem problem = error.diagnosis().problem();

      if (isToBeBlamed(problem, blamableProblems)) {
        return blame.apply(error.diagnosis());
      } else {
        return OurFault.of(error.diagnosis());
      }
    };
  }

  private static boolean isToBeBlamed(Problem problem, Collection<Problem> blamableProblems) {
    return blamableProblems.contains(problem);
  }
}
