package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Team;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Either;

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
