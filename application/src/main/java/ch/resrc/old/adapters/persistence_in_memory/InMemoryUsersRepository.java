package ch.resrc.old.adapters.persistence_in_memory;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import io.vavr.collection.*;
import io.vavr.control.*;

public class InMemoryUsersRepository implements AddUser, GetAllUsers {

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
