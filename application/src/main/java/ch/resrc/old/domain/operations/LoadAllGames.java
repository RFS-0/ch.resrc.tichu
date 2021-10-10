package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

@FunctionalInterface
public interface LoadAllGames extends OutputBoundary {

  Either<LoadAllGamesProblem, Set<Game>> loadAll();

  class LoadAllGamesProblem extends Problem {

    public static LoadAllGamesProblem loadFailed() {
      return (LoadAllGamesProblem) aProblem()
        .withTitle("Load failed")
        .withDetails("Could not load all games because of some unknown error")
        .withCausedBy(null)
        .build();
    }
  }
}
