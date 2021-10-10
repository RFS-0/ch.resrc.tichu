package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

@FunctionalInterface
public interface SaveTeams extends OutputBoundary {

  Either<SaveTeamsProblem, Set<Team>> save(Set<Team> existing, Seq<Team> toBeAdded);

  class SaveTeamsProblem extends Problem {

    public static SaveTeamsProblem sideEffectProblem(Problem causedBy) {
      return (SaveTeamsProblem) aProblem()
        .withTitle("Save failed")
        .withDetails("Could not save teams because of some unknown error")
        .withCausedBy(causedBy)
        .build();
    }
  }
}
