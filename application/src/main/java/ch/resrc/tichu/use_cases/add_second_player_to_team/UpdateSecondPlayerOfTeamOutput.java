package ch.resrc.tichu.use_cases.add_second_player_to_team;

import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.common.output.GameDocument;

public class UpdateSecondPlayerOfTeamOutput implements UseCaseOutput {

  private final GameDocument toBePresented;

  public UpdateSecondPlayerOfTeamOutput(GameDocument toBePresented) {
    this.toBePresented = toBePresented;
  }

  public GameDocument toBePresented() {
    return toBePresented;
  }
}
