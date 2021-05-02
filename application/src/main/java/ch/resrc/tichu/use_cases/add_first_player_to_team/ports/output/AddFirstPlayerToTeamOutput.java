package ch.resrc.tichu.use_cases.add_first_player_to_team.ports.output;

import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;

@FunctionalInterface
public interface AddFirstPlayerToTeamOutput extends UseCaseOutput {

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
