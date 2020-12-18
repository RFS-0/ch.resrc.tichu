package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;

public class InMemoryPlayersRepository implements AddPlayer, GetAllPlayers {

  private Set<Player> players = HashSet.empty();

  @Override
  public Either<PersistenceProblem, Set<Player>> add(Set<Player> existing, Player toBeAdded) {
    players = existing.add(toBeAdded);
    return Either.right(players);
  }

  @Override
  public Either<? extends Problem, Set<Player>> getAll() {
    return Either.right(HashSet.ofAll(players));
  }

}
