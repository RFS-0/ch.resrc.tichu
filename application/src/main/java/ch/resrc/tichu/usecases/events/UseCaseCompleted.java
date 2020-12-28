package ch.resrc.tichu.usecases.events;


import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.usecases.UseCasePort;
import ch.resrc.tichu.usecases.results.UseCaseResult;

public class UseCaseCompleted extends Event {

  private final Class<? extends UseCasePort> useCase;
  private final UseCaseResult result;

  private UseCaseCompleted(Class<? extends UseCasePort> useCase, UseCaseResult result) {
    super();
    this.useCase = useCase;
    this.result = result;
  }

  public static UseCaseCompleted of(Class<? extends UseCasePort> useCase, UseCaseResult result) {
    return new UseCaseCompleted(useCase, result);
  }

  public Class<? extends UseCasePort> useCase() {
    return useCase;
  }

  public UseCaseResult result() {
    return result;
  }

  public boolean is(UseCaseResult result) {
    return this.result.equals(result);
  }
}
