package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.control.Either;

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
