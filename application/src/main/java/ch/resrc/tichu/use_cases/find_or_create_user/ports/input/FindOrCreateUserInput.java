package ch.resrc.tichu.use_cases.find_or_create_user.ports.input;

import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.FindOrCreateUserOutput;
import ch.resrc.tichu.use_cases.ports.input_boundary.InputBoundary;

@FunctionalInterface
public interface FindOrCreateUserInput extends InputBoundary {

  FindOrCreateUserOutput.Response apply(Request requested);

  class Request {

    private final IntendedUser intent;

    public Request(IntendedUser intent) {
      this.intent = intent;
    }

    public IntendedUser intent() {
      return intent;
    }
  }
}
