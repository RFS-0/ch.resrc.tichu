package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.Team;
import io.vavr.collection.Set;
import io.vavr.control.Either;

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
