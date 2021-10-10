package ch.resrc.old.adapters.persistence_micro_stream;

import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;

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
