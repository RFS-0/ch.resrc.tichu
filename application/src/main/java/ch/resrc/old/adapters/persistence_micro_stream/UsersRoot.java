package ch.resrc.old.adapters.persistence_micro_stream;

import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;

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
