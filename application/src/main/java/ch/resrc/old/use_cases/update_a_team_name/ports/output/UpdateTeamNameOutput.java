package ch.resrc.old.use_cases.update_a_team_name.ports.output;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

@FunctionalInterface
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
