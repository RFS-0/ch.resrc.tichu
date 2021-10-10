package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
