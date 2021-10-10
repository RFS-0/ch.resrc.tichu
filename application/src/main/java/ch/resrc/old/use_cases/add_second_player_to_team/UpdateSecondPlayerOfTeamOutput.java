package ch.resrc.old.use_cases.add_second_player_to_team;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

public class UpdateSecondPlayerOfTeamOutput implements UseCaseOutput {

  private final GameDocument toBePresented;

  public UpdateSecondPlayerOfTeamOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
