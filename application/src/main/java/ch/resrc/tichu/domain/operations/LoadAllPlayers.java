package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Player;
import io.vavr.collection.Set;
import io.vavr.control.Either;

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
