package ch.resrc.tichu.use_cases.teams.update_team_name.ports;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.output.GameDocument;
import ch.resrc.tichu.use_cases.teams.update_team_name.ports.input.IntendedTeamName;
import ch.resrc.tichu.use_cases.teams.update_team_name.ports.input.UpdateTeamNameInput;
import ch.resrc.tichu.use_cases.teams.update_team_name.ports.output.UpdateTeamNameOutput;
import io.vavr.collection.Set;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAMES_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

public class UpdateTeamNameUseCase implements UpdateTeamNameInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;
  private final GetAllTeams getAllTeams;
  private final UpdateTeam updateTeam;

  public UpdateTeamNameUseCase(GetAllGames getAllGames, UpdateGame updateGame, GetAllTeams getAllTeams, UpdateTeam updateTeam) {
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
