package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.create_game.CreateGameInput;
import ch.resrc.tichu.use_cases.create_game.CreateGameOutput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

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
