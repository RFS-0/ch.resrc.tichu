package ch.resrc.tichu.capabilities.errorhandling;

import ch.resrc.tichu.capabilities.errorhandling.faults.Defect;
import io.vavr.collection.List;

import static java.lang.String.format;

/**
 * Signals an error where the reason for the error is a particular {@link Problem} out of a fixed set of possible {@code Problem}s. The
 * {@link #choices()} method returns the possible problems. Cannot be instantiated unless the signaled problem is one of the possible
 * problems.
 * <p>
 * This class is essentially a surrogate for union types that exist in some functional programming languages. It allows us to model the
 * possible errors that can be raised by an operations as a single error object that explicitly communicates the possible problems that
 * it can represent. In a functional programming language such errors could be modelled more elegantly with union types.
 * </p>
 */
public abstract class ProblemChoice implements HavingDiagnosis, HavingExceptionRepresentation {

  private final ProblemDiagnosis signaledProblem;

  protected ProblemChoice(ProblemDiagnosis signaledProblem) {
    requireOneOf(this.choices(), signaledProblem.problem());
    this.signaledProblem = signaledProblem;
  }

  private static void requireOneOf(List<Problem> possibleProblems, Problem signaledProblem) {
    if (!possibleProblems.contains(signaledProblem)) {
      throw Defect.of(format("Undeclared problem choice: <%s>", signaledProblem.detailsTemplate()));
    }

  }

  /**
   * @return the possible problems that this object can signal
   */
  protected abstract List<Problem> choices();

  /**
   * @return the {@link ProblemDiagnosis} that describes the problem that this object signals.
   */
  @Override
  public ProblemDiagnosis diagnosis() {
    return signaledProblem;
  }

  @Override
  public ProblemDetected asException() {

    return ProblemDetected.of(this.diagnosis());
  }

}
