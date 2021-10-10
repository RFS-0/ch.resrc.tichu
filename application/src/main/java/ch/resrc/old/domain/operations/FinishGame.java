package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.use_cases.finish_game.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
