package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.domain.entities.Team;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

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
