package ch.resrc.old.use_cases.update_card_points_of_round.ports.input;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.update_card_points_of_round.ports.output.*;

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
