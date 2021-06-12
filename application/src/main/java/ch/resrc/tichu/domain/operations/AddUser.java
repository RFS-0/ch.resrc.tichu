package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.OutputBoundary;
import ch.resrc.tichu.domain.entities.User;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface AddUser extends OutputBoundary {

  Either<? extends Problem, Set<User>> add(Set<User> existing, User toBeAdded);
}
