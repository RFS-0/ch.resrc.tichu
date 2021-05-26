package ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.output;

import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;

@FunctionalInterface
public interface RemoveSecondPlayerFromTeamOutput {

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
