package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

public interface GetAllPlayers extends OutputBoundary {

  Either<? extends Problem, Set<Player>> getAll();
}
