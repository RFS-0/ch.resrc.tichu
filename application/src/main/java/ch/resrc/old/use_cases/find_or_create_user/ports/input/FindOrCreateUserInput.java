package ch.resrc.old.use_cases.find_or_create_user.ports.input;

import ch.resrc.old.domain.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;

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
