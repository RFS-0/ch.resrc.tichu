package ch.resrc.tichu.use_cases.add_first_player_to_team;

import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.common.output.GameDocument;

public class UpdateFirstPlayerOfTeamOutput implements UseCaseOutput {

  private final GameDocument toBePresented;

  public UpdateFirstPlayerOfTeamOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
