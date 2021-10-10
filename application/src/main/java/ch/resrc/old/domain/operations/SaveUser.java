package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
