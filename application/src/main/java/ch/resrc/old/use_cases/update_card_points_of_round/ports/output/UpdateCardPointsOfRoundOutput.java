package ch.resrc.old.use_cases.update_card_points_of_round.ports.output;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

@FunctionalInterface
public interface UpdateCardPointsOfRoundOutput {

  GameDocument get();

  class Response implements UseCaseOutput {

    private final GameDocument toBePresented;

    public Response(GameDocument toBePresented) {
      this.toBePresented = toBePresented;
    }

    public GameDocument toBePresented() {
      return toBePresented;
    }
  }
}
