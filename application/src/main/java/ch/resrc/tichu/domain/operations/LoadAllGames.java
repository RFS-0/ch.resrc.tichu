package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Game;
import io.vavr.collection.Set;
import io.vavr.control.Either;

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
