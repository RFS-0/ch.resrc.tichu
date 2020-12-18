package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.domain.entities.Player;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

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
