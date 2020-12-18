package ch.resrc.tichu.capabilities.errorhandling.faults;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis.aProblemDiagnosis;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;

/**
 * Signals that a third party system failed or misbehaved.
 */
public final class TheirFault extends Fault {

  private TheirFault(TheirFault other) {
    super(other);
  }

  private TheirFault(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

  public static TheirFault of(ProblemDiagnosis diagnosed) {
    return new TheirFault(diagnosed);
  }

  public static TheirFault of(Problem detected) {
    return TheirFault.of(aProblemDiagnosis().withProblem(detected));
  }

  @Override
  protected TheirFault copy() {
    return new TheirFault(this);
  }
}
