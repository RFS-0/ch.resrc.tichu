package ch.resrc.old.use_cases.remove_second_player_from_team;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.use_cases.common.input.*;
import ch.resrc.old.use_cases.common.output.*;
import ch.resrc.old.use_cases.remove_second_player_from_team.ports.input.*;
import ch.resrc.old.use_cases.remove_second_player_from_team.ports.output.*;
import io.vavr.collection.*;

import static ch.resrc.old.capabilities.errorhandling.DomainProblem.*;
import static ch.resrc.old.capabilities.errorhandling.DomainProblemDetected.*;
import static ch.resrc.old.capabilities.errorhandling.PersistenceProblem.*;

public class RemoveSecondPlayerFromTeamUseCase implements RemoveSecondPlayerFromTeamInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;
  private final GetAllTeams getAllTeams;
  private final UpdateTeam updateTeam;

  public RemoveSecondPlayerFromTeamUseCase(GetAllGames getAllGames, UpdateGame updateGame, GetAllTeams getAllTeams, UpdateTeam updateTeam) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
    this.getAllTeams = getAllTeams;
    this.updateTeam = updateTeam;
  }

  @Override
  public RemoveSecondPlayerFromTeamOutput.Response apply(Request requested) {
    IntendedPlayerRemoval intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToRemovePlayerFrom = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Team teamToRemovePlayerFrom = gameToRemovePlayerFrom.teams()
      .find(team -> team.id().equals(intent.teamId()))
      .getOrElseThrow(supplierFor(TEAM_NOT_FOUND));

    Team updatedTeam = teamToRemovePlayerFrom.butSecondPlayer(null);

    Set<Team> existingTeams = getAllTeams.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    updateTeam.update(existingTeams, updatedTeam).getOrElseThrow(supplierFor(TEAM_NOT_UPDATED));

    Game updatedGame = gameToRemovePlayerFrom.butTeam(updatedTeam);

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new RemoveSecondPlayerFromTeamOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
