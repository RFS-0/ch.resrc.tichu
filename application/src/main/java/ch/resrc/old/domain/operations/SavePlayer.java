package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
