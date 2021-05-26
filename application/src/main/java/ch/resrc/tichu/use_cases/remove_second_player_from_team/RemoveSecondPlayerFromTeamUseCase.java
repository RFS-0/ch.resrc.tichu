package ch.resrc.tichu.use_cases.remove_second_player_from_team;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerRemoval;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.input.RemoveSecondPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.output.RemoveSecondPlayerFromTeamOutput;
import io.vavr.collection.Set;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

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
