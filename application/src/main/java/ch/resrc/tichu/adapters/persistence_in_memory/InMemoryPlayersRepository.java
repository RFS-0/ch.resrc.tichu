package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.UpdatePlayer;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class InMemoryPlayersRepository implements AddPlayer, GetAllPlayers, UpdatePlayer {

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

  @Override
  public Either<? extends Problem, Set<Player>> update(Set<Player> existing, Player updatedPlayer) {
    Option<Player> maybeToBeUpdated = existing.find(team -> team.id().equals(updatedPlayer.id()));
    if (!maybeToBeUpdated.isDefined()) {
      return Either.left(PersistenceProblem.UPDATE_FAILED_DUE_TO_MISSING_ENTITY);
    }

    Player teamToUpdate = maybeToBeUpdated.get();

    players = existing.remove(teamToUpdate);
    players = players.add(updatedPlayer);

    return Either.right(players);
  }
}
