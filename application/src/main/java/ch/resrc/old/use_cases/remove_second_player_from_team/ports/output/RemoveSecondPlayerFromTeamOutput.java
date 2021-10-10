package ch.resrc.old.use_cases.remove_second_player_from_team.ports.output;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

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
