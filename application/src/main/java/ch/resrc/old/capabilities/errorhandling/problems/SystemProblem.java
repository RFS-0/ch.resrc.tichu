package ch.resrc.old.capabilities.errorhandling.problems;

public class SystemProblem extends Problem {

  public static SystemProblem internalFailure() {
    return (SystemProblem) aProblem()
      .withTitle("Internal system problem")
      .withDetails("The system is currently unable to serve your request.")
      .withCausedBy(null)
      .build();
  }

  public static SystemProblem communicationFailure() {
    return (SystemProblem) aProblem()
      .withTitle("Communication failure")
      .withDetails("The System is currently unable to communicate to some of its partner systems.")
      .withCausedBy(null)
      .build();
  }
}
