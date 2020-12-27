package ch.resrc.tichu.capabilities.errorhandling.faults;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis.aProblemDiagnosis;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;

/**
 * Communication with a third party system or the client failed because there is a problem with the communication infrastructure. We
 * typically don't know whether our partner system received our request or what state it is in or what its response was.
 */
public final class CommunicationFault extends Fault {


  private CommunicationFault(CommunicationFault other) {
    super(other);
  }

  private CommunicationFault(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

  public static CommunicationFault of(ProblemDiagnosis diagnosed) {
    return new CommunicationFault(diagnosed);
  }

  public static CommunicationFault of(Problem detected) {
    return CommunicationFault.of(aProblemDiagnosis().withProblem(detected));
  }

  @Override
  protected CommunicationFault copy() {
    return new CommunicationFault(this);
  }

}
