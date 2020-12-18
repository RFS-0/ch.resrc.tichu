package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.GetAllGames;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;

public class InMemoryGamesRepository implements AddGame, GetAllGames {

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
}