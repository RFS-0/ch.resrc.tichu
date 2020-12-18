package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;


public class InMemoryTeamsRepository implements AddTeam, GetAllTeams {

  private Set<Team> teams = HashSet.empty();

  @Override
  public Either<? extends Problem, Set<Team>> add(Set<Team> existing, Team toBeAdded) {
    teams = existing.add(toBeAdded);
    return Either.right(teams);
  }

  @Override
  public Either<PersistenceProblem, Set<Team>> getAll() {
    return Either.right(HashSet.ofAll(teams));
  }
}
