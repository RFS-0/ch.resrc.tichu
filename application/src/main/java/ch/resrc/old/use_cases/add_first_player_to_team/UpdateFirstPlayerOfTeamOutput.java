package ch.resrc.old.use_cases.add_first_player_to_team;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

public class UpdateFirstPlayerOfTeamOutput implements UseCaseOutput {

  private final GameDocument toBePresented;

  public UpdateFirstPlayerOfTeamOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
