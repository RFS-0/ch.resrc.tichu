package ch.resrc.tichu.use_cases.add_second_player_to_team.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.add_second_player_to_team.ports.output.AddSecondPlayerToTeamOutput;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;

@FunctionalInterface
public interface AddSecondPlayerToTeamInput {

  AddSecondPlayerToTeamOutput.Response apply(Request requested);

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
