package ch.resrc.tichu.capabilities.errorhandling.problems;

public class UseCaseProblem extends Problem {

  public static UseCaseProblem invalidInputDetected() {
    return (UseCaseProblem) aProblem()
      .withTitle("Invalid input")
      .withDetails("${validationMessage}")
      .withCausedBy(null)
      .build();
  }
}
