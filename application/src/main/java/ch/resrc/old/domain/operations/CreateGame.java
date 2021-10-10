package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.use_cases.create_game.*;
import io.vavr.collection.*;
import io.vavr.control.*;

@FunctionalInterface
public interface CreateGame {

  Either<CreateGameProblem, CreateGameOutput> invoke(CreateGameInput input);

  class CreateGameProblem extends Problem {

    public static CreateGameProblem operationFailed(Problem causedBy) {
      return (CreateGameProblem) aProblem()
        .withTitle("Operation failed")
        .withDetails("Could not find or add first player to team because operation failed")
        .withCausedBy(causedBy)
        .build();
    }

    public static CreateGameProblem validationFailed(Seq<ValidationError> errors) {
      return (CreateGameProblem) aProblem()
        .withTitle("Operation failed")
        .withDetails("Could not find or add first player to team because operation failed")
        .withCausedBy(null)
        .withValidationErrors(errors)
        .build();
    }
  }
}
