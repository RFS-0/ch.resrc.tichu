package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.control.Either;

@FunctionalInterface
public interface FindPlayerByUserId {

  Either<FindPlayerProblem, Player> find(Id userId);

  class FindPlayerProblem extends Problem {

    public static FindPlayerProblem sideEffectFailed(Problem causedBy) {
      return (FindPlayerProblem) aProblem()
        .withTitle("Side effect failed")
        .withDetails("Could not find player because side effect failed")
        .withCausedBy(causedBy)
        .build();
    }

    public static FindPlayerProblem playerCouldNotBeFound() {
      return (FindPlayerProblem) aProblem()
        .withTitle("Find failed")
        .withDetails("Could not find player")
        .withCausedBy(null)
        .build();
    }
  }
}
