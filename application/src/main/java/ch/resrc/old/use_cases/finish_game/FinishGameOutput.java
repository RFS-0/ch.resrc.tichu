package ch.resrc.old.use_cases.finish_game;

import ch.resrc.old.use_cases.common.output.*;

public class FinishGameOutput {

  private final GameDocument toBePresented;

  public FinishGameOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
