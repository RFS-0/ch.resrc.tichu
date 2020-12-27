package ch.resrc.tichu.domain.repositories;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.value_objects.Id;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface GameRepository {

  boolean shutdown();

  void insert(Game game);

  void insertAll(Collection<Game> games);

  boolean delete(Game game);

  void deleteAll();

  Set<Game> all();

  Optional<Game> findById(Id gameId);

  void update(Game game);

}
