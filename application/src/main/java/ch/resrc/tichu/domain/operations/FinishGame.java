package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.finish_game.FinishGameInput;
import ch.resrc.tichu.use_cases.finish_game.FinishGameOutput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

@FunctionalInterface
public interface FinishGame {

  Either<FinishGameProblem, FinishGameOutput> invoke(FinishGameInput input);

  class FinishGameProblem extends Problem {

    public static FinishGameProblem operationFailed(Problem causedBy) {
      return (FinishGameProblem) aProblem()
        .withTitle("Operation failed")
        .withDetails("Could not finish game because operation failed")
        .withCausedBy(causedBy)
        .build();
    }

    public static FinishGameProblem validationFailed(Seq<ValidationError> errors) {
      return (FinishGameProblem) aProblem()
        .withTitle("Operation failed")
        .withDetails("Could finish game because operation failed")
        .withCausedBy(null)
        .withValidationErrors(errors)
        .build();
    }
  }
}
