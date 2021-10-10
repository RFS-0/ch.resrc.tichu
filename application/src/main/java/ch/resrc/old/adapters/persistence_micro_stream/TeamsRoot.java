package ch.resrc.old.adapters.persistence_micro_stream;

import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;

public class TeamsRoot {

  private Set<Team> teams = HashSet.empty();

  public Set<Team> teams() {
    return teams;
  }

  public Set<Team> update(Set<Team> teams) {
    this.teams = teams;
    return teams;
  }
}
