package ch.resrc.tichu.use_cases.capabilities.events;


import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.InputBoundary;

public class UseCaseStarted extends Event {

  private final Class<? extends InputBoundary> useCase;
  private final String origin;

  public UseCaseStarted(Class<? extends InputBoundary> useCase, String origin) {
    super();
    this.useCase = useCase;
    this.origin = origin;
  }

  public Class<? extends InputBoundary> getUseCase() {
    return useCase;
  }

  public String origin() {
    return origin;
  }
}
