package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.use_cases.add_first_player_to_team.*;
import io.vavr.control.*;

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
