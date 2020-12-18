package ch.resrc.tichu.capabilities.errorhandling.faults;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis.aProblemDiagnosis;

import ch.resrc.tichu.capabilities.errorhandling.GenericProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;

/**
 * Signals that the system failed due to a problem for which the organization that runs the application is responsible.
 *
 * <p>Examples include:
 * <ul>
 *     <li>A remote resource that is controlled by the organization is unavailable</li>
 *     <li>A file on disk is unavailable or corrupt</li>
 *     <li>Data from the database is corrupt</li>
 * </ul>
 * <p>
 * If the problem is clearly due to a programming error that requires bug-fixing, throw {@link Defect} instead.
 */
public final class OurFault extends Fault {

  private final static ProblemDiagnosis systemFailure = aProblemDiagnosis().withProblem(GenericProblem.SYSTEM_FAILURE);

  private OurFault(OurFault other) {
    super(other);
  }

  private OurFault(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

  public static OurFault of(ProblemDiagnosis diagnosed) {
    return new OurFault(diagnosed);
  }

  public static OurFault of(Problem detected) {
    return OurFault.of(aProblemDiagnosis().withProblem(detected));
  }

  public static OurFault of(Throwable cause) {
    return OurFault.of(systemFailure.withDebugMessage(cause.getMessage()).withCause(cause));
  }

  @Override
  protected OurFault copy() {
    return new OurFault(this);
  }
}
