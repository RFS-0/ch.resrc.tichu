package ch.resrc.tichu.use_cases.update_rank_of_player.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.output.UpdateRankOfPlayerOutput;

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
