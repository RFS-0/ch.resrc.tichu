package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.User;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface LoadAllUsers extends OutputBoundary {

  Either<? extends Problem, Set<User>> loadAll();

  class LoadAllTeamsProblem extends Problem {

    public static LoadAllTeamsProblem loadFailed() {
      return (LoadAllTeamsProblem) aProblem()
        .withTitle("Load failed")
        .withDetails("Could not load all teams because side effect failed")
        .withCausedBy(null)
        .build();
    }
  }
}
