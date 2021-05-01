package ch.resrc.tichu.adapters.persistence_in_memory;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class InMemoryTeamsRepository implements AddTeam, GetAllTeams, UpdateTeam {

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

  @Override
  public Either<? extends Problem, Set<Team>> update(Set<Team> existing, Team updatedTeam) {
    Option<Team> maybeToBeUpdated = existing.find(team -> team.id().equals(updatedTeam.id()));
    if (!maybeToBeUpdated.isDefined()) {
      return Either.left(PersistenceProblem.UPDATE_FAILED_DUE_TO_MISSING_ENTITY);
    }

    Team teamToUpdate = maybeToBeUpdated.get();

    teams = existing.remove(teamToUpdate);
    teams = teams.add(updatedTeam);

    return Either.right(teams);
  }
}
