package ch.resrc.tichu.use_cases.finish_game.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.finish_game.ports.output.FinishGameOutput;

@FunctionalInterface
public interface FinishGameInput {

  FinishGameOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedGameFinish intent;

    public Request(IntendedGameFinish intent) {
      this.intent = intent;
    }

    public IntendedGameFinish intent() {
      return intent;
    }
  }
}
