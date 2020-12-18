package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.domain.entities.Game;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public class GamesRoot {

  private Set<Game> games = HashSet.empty();

  public Set<Game> games() {
    return games;
  }

  public Set<Game> update(Set<Game> games) {
    this.games = games;
    return games;
  }
}
