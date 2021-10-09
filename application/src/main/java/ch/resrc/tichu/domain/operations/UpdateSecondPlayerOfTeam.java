package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.use_cases.add_second_player_to_team.UpdateSecondPlayerOfTeamInput;
import ch.resrc.tichu.use_cases.add_second_player_to_team.UpdateSecondPlayerOfTeamOutput;
import io.vavr.control.Either;

@FunctionalInterface
public interface UpdateSecondPlayerOfTeam {

  Either<UpdateSecondPlayerOfTeamProblem, UpdateSecondPlayerOfTeamOutput> invoke(UpdateSecondPlayerOfTeamInput input);

  class UpdateSecondPlayerOfTeamProblem extends Problem {

    public static UpdateSecondPlayerOfTeamProblem operationFailed(Problem causedBy) {
      return (UpdateSecondPlayerOfTeamProblem) aProblem()
        .withTitle("Operation failed")
        .withDetails("Could not find or add first player to team because operation failed")
        .withCausedBy(causedBy)
        .build();
    }
  }
}
