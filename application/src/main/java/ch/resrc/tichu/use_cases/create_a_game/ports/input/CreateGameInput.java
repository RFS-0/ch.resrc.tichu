package ch.resrc.tichu.use_cases.create_a_game.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.CreateGameOutput;
import ch.resrc.tichu.use_cases.ports.input_boundary.InputBoundary;

@FunctionalInterface
public interface CreateGameInput extends InputBoundary {

  CreateGameOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedGame intent;

    public Request(IntendedGame intent) {
      this.intent = intent;
    }

    public IntendedGame intent() {
      return intent;
    }
  }
}
