package ch.resrc.tichu.use_cases.capabilities.events;

import ch.resrc.tichu.capabilities.events.Event;

import static java.lang.String.format;

public class UseCaseProblemDetected extends Event {

  private final String problem;

  private UseCaseProblemDetected(String problem) {

    this.problem = problem;
  }

  public static UseCaseProblemDetected of(String problem) {
    return new UseCaseProblemDetected(problem);
  }

  public static UseCaseProblemDetected of(Throwable problem) {
    return UseCaseProblemDetected.of(format("%s: %s", problem.getClass().getSimpleName(), problem.getMessage()));
  }

  public String problem() {
    return problem;
  }
}
