package ch.resrc.old.capabilities.errorhandling.problems;

public class ClientProblem extends Problem {

  public static ClientProblem badRequest() {
    return (ClientProblem) aProblem()
      .withTitle("Bad request")
      .withDetails("Your request fails to validate or violates preconditions.")
      .withCausedBy(null)
      .build();
  }
}
