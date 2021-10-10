package ch.resrc.old.use_cases.reset_rank_of_player.ports.output;

import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.common.output.*;

@FunctionalInterface
public interface ResetRankOfPlayerOutput {

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
