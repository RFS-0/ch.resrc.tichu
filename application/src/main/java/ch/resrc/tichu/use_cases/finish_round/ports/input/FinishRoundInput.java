package ch.resrc.tichu.use_cases.finish_round.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.finish_round.ports.output.FinishRoundOutput;

@FunctionalInterface
public interface FinishRoundInput {

  FinishRoundOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedRoundFinish intent;

    public Request(IntendedRoundFinish intent) {
      this.intent = intent;
    }

    public IntendedRoundFinish intent() {
      return intent;
    }
  }
}
