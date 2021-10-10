package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.control.*;

@FunctionalInterface
public interface FindUser {

  Either<FindUserProblem, User> find(Id userId);

  class FindUserProblem extends Problem {

    public static FindUserProblem sideEffectFailed(Problem causedBy) {
      return (FindUserProblem) aProblem()
        .withTitle("Side effect failed")
        .withDetails("Could not find user because side effect failed")
        .withCausedBy(causedBy)
        .build();
    }

    public static FindUserProblem userCouldNotBeFound() {
      return (FindUserProblem) aProblem()
        .withTitle("Find failed")
        .withDetails("Could not find user")
        .withCausedBy(null)
        .build();
    }
  }
}
