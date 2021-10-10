package ch.resrc.old.adapters.persistence_in_memory;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
