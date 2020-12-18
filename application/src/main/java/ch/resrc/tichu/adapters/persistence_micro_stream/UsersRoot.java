package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.domain.entities.User;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public class UsersRoot {

  private Set<User> users = HashSet.empty();

  public Set<User> users() {
    return users;
  }

  public Set<User> update(Set<User> users) {
    this.users = users;
    return users;
  }
}
