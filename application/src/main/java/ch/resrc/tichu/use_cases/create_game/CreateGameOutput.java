package ch.resrc.tichu.use_cases.create_game;

import ch.resrc.tichu.use_cases.common.output.GameDocument;

public class CreateGameOutput {

  private final GameDocument toBePresented;

  public CreateGameOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
