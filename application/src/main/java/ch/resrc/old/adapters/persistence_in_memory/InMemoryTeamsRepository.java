package ch.resrc.old.adapters.persistence_in_memory;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import io.vavr.collection.*;
import io.vavr.control.*;

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
