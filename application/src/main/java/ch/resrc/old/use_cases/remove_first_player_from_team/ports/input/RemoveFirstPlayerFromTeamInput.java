package ch.resrc.old.use_cases.remove_first_player_from_team.ports.input;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.input.*;
import ch.resrc.old.use_cases.remove_first_player_from_team.ports.output.*;

@FunctionalInterface
public interface RemoveFirstPlayerFromTeamInput {

  RemoveFirstPlayerFromTeamOutput.Response apply(Request requested);

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
