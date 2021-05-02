package ch.resrc.tichu.use_cases.add_first_player_to_team.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;

@FunctionalInterface
public interface AddFirstPlayerToTeamInput {

  void apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedPlayerAddition intent;

    public Request(IntendedPlayerAddition intent) {
      this.intent = intent;
    }

    public IntendedPlayerAddition intent() {
      return intent;
    }
  }
}
