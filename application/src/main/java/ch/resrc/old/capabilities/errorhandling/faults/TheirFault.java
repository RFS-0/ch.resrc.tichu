package ch.resrc.old.capabilities.errorhandling.faults;

import ch.resrc.old.capabilities.errorhandling.*;

import static ch.resrc.old.capabilities.errorhandling.ProblemDiagnosis.*;

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
