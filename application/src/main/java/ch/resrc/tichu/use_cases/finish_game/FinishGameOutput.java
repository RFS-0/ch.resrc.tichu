package ch.resrc.tichu.use_cases.finish_game;

import ch.resrc.tichu.use_cases.common.output.GameDocument;

public class FinishGameOutput {

  private final GameDocument toBePresented;

  public FinishGameOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
