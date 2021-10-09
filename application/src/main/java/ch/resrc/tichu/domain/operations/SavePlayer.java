package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Player;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface SavePlayer extends OutputBoundary {

  Either<SavePlayerProblem, Set<Player>> save(Set<Player> existing, Player toBeAdded);

  class SavePlayerProblem extends Problem {

    public static SavePlayerProblem saveFailed() {
      return (SavePlayerProblem) aProblem()
        .withTitle("Save failed")
        .withDetails("Could not safe player because of some unknown error")
        .withCausedBy(null)
        .build();
    }
  }
}
