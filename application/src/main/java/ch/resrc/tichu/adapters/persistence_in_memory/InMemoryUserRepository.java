package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;

public class InMemoryUserRepository implements AddUser, GetAllUsers {

  private Set<User> users = HashSet.empty();

  @Override
  public Either<? extends Problem, Set<User>> add(Set<User> existing, User toBeAdded) {
    users = existing.add(toBeAdded);
    return Either.right(users);
  }

  @Override
  public Either<? extends Problem, Set<User>> getAll() {
    return Either.right(HashSet.ofAll(users));
  }
}
