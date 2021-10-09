package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.User;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface SaveUser {

  Either<SaveUserProblem, Set<User>> save(Set<User> existing, User toBeAdded);

  class SaveUserProblem extends Problem {

    public static SaveUserProblem sideEffectProblem() {
      return (SaveUserProblem) aProblem()
        .withTitle("Save failed")
        .withDetails("Could not save user because side effect failed")
        .withCausedBy(null)
        .build();
    }
  }
}
