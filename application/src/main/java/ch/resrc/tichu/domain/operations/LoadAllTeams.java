package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Team;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface LoadAllTeams extends OutputBoundary {

  Either<LoadAllTeamsProblem, Set<Team>> loadAll();

  class LoadAllTeamsProblem extends Problem {

    public static LoadAllTeamsProblem sideEffectProblem() {
      return (LoadAllTeamsProblem) aProblem()
        .withTitle("Load failed")
        .withDetails("Could not load all teams because side effect failed")
        .withCausedBy(null)
        .build();
    }
  }
}
