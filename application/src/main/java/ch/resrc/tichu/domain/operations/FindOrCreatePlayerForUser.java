package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.control.Either;

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
