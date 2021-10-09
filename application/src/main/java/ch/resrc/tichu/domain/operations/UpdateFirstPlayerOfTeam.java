package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.use_cases.add_first_player_to_team.UpdateFirstPlayerOfTeamInput;
import ch.resrc.tichu.use_cases.add_first_player_to_team.UpdateFirstPlayerOfTeamOutput;
import io.vavr.control.Either;

@FunctionalInterface
public interface UpdateFirstPlayerOfTeam {

  Either<UpdateFirstPlayerOfTeamProblem, UpdateFirstPlayerOfTeamOutput> invoke(UpdateFirstPlayerOfTeamInput input);

  class UpdateFirstPlayerOfTeamProblem extends Problem {

    public static UpdateFirstPlayerOfTeamProblem operationFailed(Problem causedBy) {
      return (UpdateFirstPlayerOfTeamProblem) aProblem()
        .withTitle("Operation failed")
        .withDetails("Could not find or add first player to team because operation failed")
        .withCausedBy(causedBy)
        .build();
    }
  }
}
