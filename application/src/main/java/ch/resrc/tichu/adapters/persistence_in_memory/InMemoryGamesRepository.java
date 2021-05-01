package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.UpdateGame;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class InMemoryGamesRepository implements AddGame, GetAllGames, UpdateGame {

  private Set<Game> games = HashSet.empty();

  @Override
  public Either<PersistenceProblem, Set<Game>> add(Set<Game> existing, Game toBeAdded) {
    games = existing.add(toBeAdded);
    return Either.right(games);
  }

  @Override
  public Either<? extends Problem, Set<Game>> getAll() {
    return Either.right(HashSet.ofAll(games));
  }

  @Override
  public Either<? extends Problem, Set<Game>> update(Set<Game> existing, Game updatedGame) {
    Option<Game> maybeToBeUpdated = existing.find(game -> game.id().equals(updatedGame.id()));
    if (!maybeToBeUpdated.isDefined()) {
      return Either.left(PersistenceProblem.UPDATE_FAILED_DUE_TO_MISSING_ENTITY);
    }

    Game teamToUpdate = maybeToBeUpdated.get();

    games = existing.remove(teamToUpdate);
    games = games.add(updatedGame);

    return Either.right(games);
  }
}
