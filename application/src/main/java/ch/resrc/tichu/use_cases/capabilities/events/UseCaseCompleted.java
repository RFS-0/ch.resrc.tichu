package ch.resrc.tichu.use_cases.capabilities.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.InputBoundary;
import ch.resrc.tichu.use_cases.capabilities.results.UseCaseResult;

public class UseCaseCompleted extends Event {

  private final Class<? extends InputBoundary> useCase;
  private final UseCaseResult result;

  private UseCaseCompleted(Class<? extends InputBoundary> useCase, UseCaseResult result) {
    super();
    this.useCase = useCase;
    this.result = result;
  }

  public static UseCaseCompleted of(Class<? extends InputBoundary> useCase, UseCaseResult result) {
    return new UseCaseCompleted(useCase, result);
  }

  public Class<? extends InputBoundary> useCase() {
    return useCase;
  }

  public UseCaseResult result() {
    return result;
  }

  public boolean is(UseCaseResult result) {
    return this.result.equals(result);
  }
}
