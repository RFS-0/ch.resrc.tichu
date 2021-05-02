package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.use_cases.OutputBoundary;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface UpdatePlayer extends OutputBoundary {

  Either<? extends Problem, Set<Player>> update(Set<Player> existing, Player updatedPlayer);
}
