package ch.resrc.old.use_cases.reset_rank_of_player.ports.input;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.reset_rank_of_player.ports.output.*;

@FunctionalInterface
public interface ResetRankOfPlayerInput {

  ResetRankOfPlayerOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedPlayerRankReset intent;

    public Request(IntendedPlayerRankReset intent) {
      this.intent = intent;
    }

    public IntendedPlayerRankReset intent() {
      return intent;
    }
  }
}
