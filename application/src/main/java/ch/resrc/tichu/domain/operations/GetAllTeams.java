package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.use_cases.ports.output_boundary.OutputBoundary;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface GetAllTeams extends OutputBoundary {

  Either<? extends Problem, Set<Team>> getAll();

}
