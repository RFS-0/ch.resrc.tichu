package ch.resrc.old.capabilities.errorhandling.problems;

public class UseCaseProblem extends Problem {

  public static UseCaseProblem invalidInputDetected() {
    return (UseCaseProblem) aProblem()
      .withTitle("Invalid input")
      .withDetails("${validationMessage}")
      .withCausedBy(null)
      .build();
  }
}
