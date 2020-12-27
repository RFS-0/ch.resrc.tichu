package ch.resrc.tichu.ports.outbound.authorization;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemChoice;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import java.util.List;

/**
 * Represents a possible error that an {@link AccessControl} might return.
 */
public class AuthorizationError extends ProblemChoice {

  public AuthorizationError(ProblemDiagnosis signaledProblem) {
    super(signaledProblem);
  }

  @Override
  protected List<Problem> choices() {

    return List.of(AuthorizationProblem.ACCESS_DENIED);
  }
}
