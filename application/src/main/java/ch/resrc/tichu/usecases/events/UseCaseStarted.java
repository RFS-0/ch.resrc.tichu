package ch.resrc.tichu.usecases.events;


import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.usecases.UseCasePort;

public class UseCaseStarted extends Event {

  private final Class<? extends UseCasePort> useCase;
  private final String origin;

  public UseCaseStarted(Class<? extends UseCasePort> useCase, String origin) {
    super();
    this.useCase = useCase;
    this.origin = origin;
  }

  public Class<? extends UseCasePort> getUseCase() {
    return useCase;
  }

  public String origin() {
    return origin;
  }
}
