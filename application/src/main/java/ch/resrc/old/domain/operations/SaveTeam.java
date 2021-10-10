package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

@FunctionalInterface
public interface SaveTeam extends OutputBoundary {

  Either<SaveTeamProblem, Set<Team>> save(Set<Team> existing, Team toBeAdded);

  class SaveTeamProblem extends Problem {

    public static SaveTeamProblem sideEffectProblem(Problem causedBy) {
      return (SaveTeamProblem) aProblem()
        .withTitle("Save failed")
        .withDetails("Could not save team because of some unknown error")
        .withCausedBy(causedBy)
        .build();
    }
  }
}
