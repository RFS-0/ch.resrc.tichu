package ch.resrc.tichu.adapters.persistence.game;

import ch.resrc.tichu.domain.entities.Game;
import java.util.HashSet;
import java.util.Set;

public class GameRoot {

  private final Set<Game> games = new HashSet<>();

  public Set<Game> games() {
    return games;
  }

}
