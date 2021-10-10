package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.control.*;

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
