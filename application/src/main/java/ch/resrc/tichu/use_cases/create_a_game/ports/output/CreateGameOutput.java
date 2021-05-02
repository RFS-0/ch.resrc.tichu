package ch.resrc.tichu.use_cases.create_a_game.ports.output;

import ch.resrc.tichu.use_cases.UseCaseOutput;

public interface CreateGameOutput extends UseCaseOutput {

  GameDocument get();

  class Response implements UseCaseOutput {

    private final GameDocument toBePresented;

    public Response(GameDocument toBePresented) {
      this.toBePresented = toBePresented;
    }

    public GameDocument toBePresented() {
      return toBePresented;
    }
  }
}
