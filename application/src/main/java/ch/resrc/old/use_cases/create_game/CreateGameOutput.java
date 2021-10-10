package ch.resrc.old.use_cases.create_game;

import ch.resrc.old.use_cases.common.output.*;

public class CreateGameOutput {

  private final GameDocument toBePresented;

  public CreateGameOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
