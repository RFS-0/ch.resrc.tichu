package ch.resrc.old.use_cases.update_round.ports.input;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.update_round.ports.output.*;

@FunctionalInterface
public interface UpdateRoundInput {

  UpdateRoundOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedRoundUpdate intent;

    public Request(IntendedRoundUpdate intent) {
      this.intent = intent;
    }

    public IntendedRoundUpdate intent() {
      return intent;
    }
  }
}
