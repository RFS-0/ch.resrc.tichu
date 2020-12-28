package ch.resrc.tichu.capabilities.errorhandling.faults;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis.aProblemDiagnosis;

import ch.resrc.tichu.capabilities.errorhandling.BusinessError;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;

/**
 * It's the client's fault! Our client invoked us with invalid or malicious input or with an unacceptable intent according to the
 * business rules. The client must change its behaviour in order to avoid the error.
 */
public final class ClientFault extends Fault implements BusinessError {

  private ClientFault(ClientFault other) {
    super(other);
  }

  private ClientFault(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

  public static ClientFault of(ProblemDiagnosis diagnosed) {
    return new ClientFault(diagnosed);
  }

  public static ClientFault of(Problem detected) {
    return ClientFault.of(aProblemDiagnosis().withProblem(detected));
  }

  @Override
  protected ClientFault copy() {
    return new ClientFault(this);
  }

  @Override
  public ClientFault asException() {
    return this;
  }
}
