package ch.resrc.tichu.use_cases.add_first_player_to_team;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.add_first_player_to_team.ports.input.AddFirstPlayerToTeamInput;
import ch.resrc.tichu.use_cases.add_first_player_to_team.ports.output.AddFirstPlayerToTeamOutput;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.time.Instant;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.PLAYER_NOT_SAVED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

public class AddFirstPlayerToTeamUseCase implements AddFirstPlayerToTeamInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;
  private final GetAllTeams getAllTeams;
  private final UpdateTeam updateTeam;
  private final GetAllPlayers getAllPlayers;
  private final AddPlayer addPlayer;

  public AddFirstPlayerToTeamUseCase(GetAllGames getAllGames,
                                     UpdateGame updateGame,
                                     GetAllTeams getAllTeams,
                                     UpdateTeam updateTeam,
                                     GetAllPlayers getAllPlayers,
                                     AddPlayer addPlayer) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
    this.getAllTeams = getAllTeams;
    this.updateTeam = updateTeam;
    this.getAllPlayers = getAllPlayers;
    this.addPlayer = addPlayer;
  }

  @Override
  public AddFirstPlayerToTeamOutput.Response apply(Request requested) {
    IntendedPlayerAddition intent = requested.intent();

    Set<Player> existingPlayers = getAllPlayers.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Player playerToAdd;
    Option<Player> maybeExistingPlayer = existingPlayers.find(player -> player.id().equals(intent.userId()));
    if (maybeExistingPlayer.isDefined()) {
      playerToAdd = maybeExistingPlayer.get();
    } else {
      playerToAdd = Player.create(Id.next(), intent.playerName(), Instant.now()).get();
      addPlayer.add(existingPlayers, playerToAdd).getOrElseThrow(supplierFor(PLAYER_NOT_SAVED));
    }

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToAddPlayerTo = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Team teamToAddPlayerTo = gameToAddPlayerTo.teams()
      .find(team -> team.id().equals(intent.teamId()))
      .getOrElseThrow(supplierFor(TEAM_NOT_FOUND));

    Team updatedTeam = teamToAddPlayerTo.butFirstPlayer(playerToAdd);

    Set<Team> existingTeams = getAllTeams.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    updateTeam.update(existingTeams, updatedTeam).getOrElseThrow(supplierFor(TEAM_NOT_UPDATED));

    Game updatedGame = gameToAddPlayerTo.butTeam(updatedTeam);

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new AddFirstPlayerToTeamOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
