package ch.resrc.tichu.capabilities.errorhandling;

import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.errorhandling.faults.Fault;
import ch.resrc.tichu.capabilities.errorhandling.faults.OurFault;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static ch.resrc.tichu.use_cases.capabilities.errors.UseCaseProblem.INVALID_INPUT_DETECTED;
import static io.vavr.Predicates.instanceOf;

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

  public static <X extends RuntimeException, Y extends X> Consumer<X> throwUnless(Class<Y> noThrow) throws X {
    return (X error) -> {
      if (noThrow.isAssignableFrom(error.getClass())) {
        return;
      }

      throw error;
    };
  }

  public static ClientFault asClientFault(Seq<ValidationError> validationError) {
    return ClientFault.of(
      ProblemDiagnosis.of(INVALID_INPUT_DETECTED).withContext("validationMessage", validationError)
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
