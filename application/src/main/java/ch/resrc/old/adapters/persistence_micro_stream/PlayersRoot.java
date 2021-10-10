package ch.resrc.old.adapters.persistence_micro_stream;

import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;

public class PlayersRoot {

  private Set<Player> players = HashSet.empty();

  public Set<Player> players() {
    return players;
  }

  public Set<Player> update(Set<Player> players) {
    this.players = players;
    return players;
  }

}
