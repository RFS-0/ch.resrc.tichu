package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.control.*;

@FunctionalInterface
public interface FindOrCreatePlayerForUser {

  Either<? extends Problem, Player> findOrCreate(Id userId, Name playerName);

  class FindOrCreatePlayerForUserProblem extends Problem {

    public static FindOrCreatePlayerForUserProblem sideEffectProblem(Problem causedBy) {
      return (FindOrCreatePlayerForUserProblem) aProblem()
        .withTitle("Side effect failed")
        .withDetails("Could not find or create user because side effect failed")
        .withCausedBy(causedBy)
        .build();
    }
  }
}
