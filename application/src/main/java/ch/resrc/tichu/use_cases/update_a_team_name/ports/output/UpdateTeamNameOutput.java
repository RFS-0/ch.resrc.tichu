package ch.resrc.tichu.use_cases.update_a_team_name.ports.output;

import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;

public interface UpdateTeamNameOutput extends UseCaseOutput {

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
