package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

@FunctionalInterface
public interface UpdatePlayer extends OutputBoundary {

  Either<? extends Problem, Set<Player>> update(Set<Player> existing, Player updatedPlayer);
}
