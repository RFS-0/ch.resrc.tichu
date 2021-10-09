package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.control.Either;

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
