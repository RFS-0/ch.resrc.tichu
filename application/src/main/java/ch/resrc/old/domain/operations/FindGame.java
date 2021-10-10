package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.control.*;

@FunctionalInterface
public interface FindGame {

  Either<FindGameProblem, Game> find(Id gameId);

  class FindGameProblem extends Problem {

    public static FindGameProblem sideEffectFailed(Problem causedBy) {
      return (FindGameProblem) aProblem()
        .withTitle("Side effect failed")
        .withDetails("Could not find game because side effect failed")
        .withCausedBy(causedBy)
        .build();
    }

    public static FindGameProblem gameCouldNotBeFound() {
      return (FindGameProblem) aProblem()
        .withTitle("Find failed")
        .withDetails("Could not find game")
        .withCausedBy(null)
        .build();
    }
  }
}
