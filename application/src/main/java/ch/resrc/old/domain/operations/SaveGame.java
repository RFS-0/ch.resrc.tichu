package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
