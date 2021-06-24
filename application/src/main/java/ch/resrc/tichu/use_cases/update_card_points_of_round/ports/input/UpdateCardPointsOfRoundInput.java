package ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.output.UpdateCardPointsOfRoundOutput;

public interface UpdateCardPointsOfRoundInput {

  UpdateCardPointsOfRoundOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedCardPointsUpdate intent;

    public Request(IntendedCardPointsUpdate intent) {
      this.intent = intent;
    }

    public IntendedCardPointsUpdate intent() {
      return intent;
    }
  }
}
