package ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.common.input.IntendedPlayerRemoval;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.output.RemoveSecondPlayerFromTeamOutput;

@FunctionalInterface
public interface RemoveSecondPlayerFromTeamInput {

  RemoveSecondPlayerFromTeamOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedPlayerRemoval intent;

    public Request(IntendedPlayerRemoval intent) {
      this.intent = intent;
    }

    public IntendedPlayerRemoval intent() {
      return intent;
    }
  }
}
