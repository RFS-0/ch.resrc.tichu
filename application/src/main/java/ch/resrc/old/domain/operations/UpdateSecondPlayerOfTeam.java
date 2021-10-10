package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.use_cases.add_second_player_to_team.*;
import io.vavr.control.*;

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
