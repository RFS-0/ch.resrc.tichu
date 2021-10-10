package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

public interface LoadAllPlayers extends OutputBoundary {

  Either<LoadAllPlayersProblem, Set<Player>> loadAll();

  class LoadAllPlayersProblem extends Problem {

    public static LoadAllPlayersProblem loadFailed() {
      return (LoadAllPlayersProblem) aProblem()
        .withTitle("Load failed")
        .withDetails("Could not load all players because of some unknown error")
        .withCausedBy(null)
        .build();
    }
  }
}
