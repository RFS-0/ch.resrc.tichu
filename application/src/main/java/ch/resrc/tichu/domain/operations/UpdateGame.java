package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.use_cases.OutputBoundary;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface UpdateGame extends OutputBoundary {

  Either<? extends Problem, Set<Game>> update(Set<Game> existing, Game updatedGame);
}
