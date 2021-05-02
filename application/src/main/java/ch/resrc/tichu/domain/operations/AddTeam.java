package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.use_cases.OutputBoundary;
import io.vavr.collection.Set;
import io.vavr.control.Either;

@FunctionalInterface
public interface AddTeam extends OutputBoundary {

  Either<? extends Problem, Set<Team>> add(Set<Team> existing, Team toBeAdded);

}
