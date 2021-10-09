package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Game;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface SaveGame extends OutputBoundary {

  Either<SaveGameProblem, Set<Game>> save(Set<Game> existing, Game toBeAdded);

  class SaveGameProblem extends Problem {

    public static SaveGameProblem saveFailed() {
      return (SaveGameProblem) aProblem()
        .withTitle("Save failed")
        .withDetails("Could not save game because of some unknown error")
        .withCausedBy(null)
        .build();
    }
  }
}
