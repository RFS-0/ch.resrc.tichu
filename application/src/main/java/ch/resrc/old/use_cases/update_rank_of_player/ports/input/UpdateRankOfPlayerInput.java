package ch.resrc.old.use_cases.update_rank_of_player.ports.input;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.update_rank_of_player.ports.output.*;

@FunctionalInterface
public interface UpdateRankOfPlayerInput {

  UpdateRankOfPlayerOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedPlayerRankUpdate intent;

    public Request(IntendedPlayerRankUpdate intent) {
      this.intent = intent;
    }

    public IntendedPlayerRankUpdate intent() {
      return intent;
    }
  }
}
