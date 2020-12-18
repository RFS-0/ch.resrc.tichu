package ch.resrc.tichu.capabilities.errorhandling.faults;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis.aProblemDiagnosis;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;

/**
 * Signals that an invocation of a third party system failed and the third party blames our system for having sent a bad request.
 */
public final class TheyBlameUs extends Fault {

  private TheyBlameUs(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

  private TheyBlameUs(TheyBlameUs other) {
    super(other);
  }

  public static TheyBlameUs of(ProblemDiagnosis diagnosed) {
    return new TheyBlameUs(diagnosed);
  }

  public static TheyBlameUs of(Problem detected) {
    return TheyBlameUs.of(aProblemDiagnosis().withProblem(detected));
  }

  @Override
  protected TheyBlameUs copy() {
    return new TheyBlameUs(this);
  }

}
