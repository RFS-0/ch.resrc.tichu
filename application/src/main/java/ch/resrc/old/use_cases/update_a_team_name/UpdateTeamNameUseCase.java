package ch.resrc.old.use_cases.update_a_team_name;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.use_cases.common.output.*;
import ch.resrc.old.use_cases.update_a_team_name.ports.input.*;
import ch.resrc.old.use_cases.update_a_team_name.ports.output.*;
import io.vavr.collection.*;

import static ch.resrc.old.capabilities.errorhandling.DomainProblem.*;
import static ch.resrc.old.capabilities.errorhandling.DomainProblemDetected.*;
import static ch.resrc.old.capabilities.errorhandling.PersistenceProblem.*;

public class UpdateTeamNameUseCase implements UpdateTeamNameInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;
  private final GetAllTeams getAllTeams;
  private final UpdateTeam updateTeam;

  public UpdateTeamNameUseCase(GetAllGames getAllGames,
                               UpdateGame updateGame,
                               GetAllTeams getAllTeams,
                               UpdateTeam updateTeam) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
    this.getAllTeams = getAllTeams;
    this.updateTeam = updateTeam;
  }

  @Override
  public UpdateTeamNameOutput.Response apply(Request requested) {
    IntendedTeamName intent = requested.intent();

    Set<Team> existingTeams = getAllTeams.getAll().getOrElseThrow(supplierFor(READ_FAILED));
    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Team teamToRename = existingTeams
      .find(team -> team.id().equals(intent.teamId()))
      .getOrElseThrow(supplierFor(TEAM_NOT_FOUND));

    Game gameToUpdate = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAMES_NOT_FOUND));

    Team renamedTeam = teamToRename.butName(intent.teamName());
    Game updatedGame = gameToUpdate.butTeam(renamedTeam);

    updateTeam.update(existingTeams, renamedTeam).getOrElseThrow(supplierFor(TEAM_NOT_UPDATED));
    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(TEAM_NOT_UPDATED));

    return new UpdateTeamNameOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
