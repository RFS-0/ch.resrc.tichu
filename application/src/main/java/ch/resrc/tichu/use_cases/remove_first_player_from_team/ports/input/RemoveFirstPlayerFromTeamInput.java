package ch.resrc.tichu.use_cases.remove_first_player_from_team.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerRemoval;

@FunctionalInterface
public interface RemoveFirstPlayerFromTeamInput {

  void apply(Request requested);

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