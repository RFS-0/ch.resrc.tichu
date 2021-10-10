package ch.resrc.old.use_cases.remove_first_player_from_team.ports.output;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

@FunctionalInterface
public interface RemoveFirstPlayerFromTeamOutput {

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
