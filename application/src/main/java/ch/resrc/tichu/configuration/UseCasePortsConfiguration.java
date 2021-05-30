package ch.resrc.tichu.configuration;

import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import ch.resrc.tichu.use_cases.add_first_player_to_team.AddFirstPlayerToTeamUseCase;
import ch.resrc.tichu.use_cases.add_first_player_to_team.ports.input.AddFirstPlayerToTeamInput;
import ch.resrc.tichu.use_cases.add_second_player_to_team.AddSecondPlayerToTeamUseCase;
import ch.resrc.tichu.use_cases.add_second_player_to_team.ports.input.AddSecondPlayerToTeamInput;
import ch.resrc.tichu.use_cases.create_a_game.CreateGameUseCase;
import ch.resrc.tichu.use_cases.find_or_create_user.FindOrCreateUserUseCase;
import ch.resrc.tichu.use_cases.remove_first_player_from_team.RemoveFirstPlayerFromTeamUseCase;
import ch.resrc.tichu.use_cases.remove_first_player_from_team.ports.input.RemoveFirstPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.RemoveSecondPlayerFromTeamUseCase;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.input.RemoveSecondPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.ResetRankOfPlayerUseCase;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.ResetRankOfPlayerInput;
import ch.resrc.tichu.use_cases.update_a_team_name.UpdateTeamNameUseCase;
import ch.resrc.tichu.use_cases.update_rank_of_player.UpdateRankOfPlayerUseCase;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.UpdateRankOfPlayerInput;

import javax.enterprise.context.ApplicationScoped;

public final class UseCasePortsConfiguration {

  @ApplicationScoped
  public CreateGameUseCase createGame(GetAllGames getAllGames,
                                      AddGame addGame,
                                      GetAllTeams getAllTeams,
                                      AddTeam addTeam,
                                      GetAllPlayers getAllPlayers,
                                      AddPlayer addPlayer,
                                      GetAllUsers getAllUsers) {
    return new CreateGameUseCase(
      getAllGames,
      addGame,
      getAllTeams,
      addTeam,
      getAllPlayers,
      addPlayer,
      getAllUsers
    );
  }

  @ApplicationScoped
  public UpdateTeamNameUseCase updateTeamName(GetAllGames getAllGames,
                                              UpdateGame updateGame,
                                              GetAllTeams getAllTeams,
                                              UpdateTeam updateTeam) {
    return new UpdateTeamNameUseCase(
      getAllGames,
      updateGame,
      getAllTeams,
      updateTeam
    );
  }

  @ApplicationScoped
  public FindOrCreateUserUseCase findOrCreateUser(GetAllUsers getAllUsers,
                                                  AddUser addUser,
                                                  GetAllPlayers getAllPlayers,
                                                  AddPlayer addPlayer) {
    return new FindOrCreateUserUseCase(
      getAllUsers,
      addUser,
      getAllPlayers,
      addPlayer
    );
  }

  @ApplicationScoped
  public AddFirstPlayerToTeamInput removeFirstPlayerFromTeamInput(GetAllGames getAllGames,
                                                                  UpdateGame updateGame,
                                                                  GetAllTeams getAllTeams,
                                                                  UpdateTeam updateTeam,
                                                                  GetAllPlayers getAllPlayers,
                                                                  AddPlayer addPlayer) {
    return new AddFirstPlayerToTeamUseCase(
      getAllGames,
      updateGame,
      getAllTeams,
      updateTeam,
      getAllPlayers,
      addPlayer
    );
  }

  @ApplicationScoped
  public AddSecondPlayerToTeamInput addSecondPlayerToTeamInput(GetAllGames getAllGames,
                                                               UpdateGame updateGame,
                                                               GetAllTeams getAllTeams,
                                                               UpdateTeam updateTeam,
                                                               GetAllPlayers getAllPlayers,
                                                               AddPlayer addPlayer) {
    return new AddSecondPlayerToTeamUseCase(
      getAllGames,
      updateGame,
      getAllTeams,
      updateTeam,
      getAllPlayers,
      addPlayer
    );
  }

  @ApplicationScoped
  public RemoveFirstPlayerFromTeamInput removeFirstPlayerFromTeamInput(GetAllGames getAllGames,
                                                                       UpdateGame updateGame,
                                                                       GetAllTeams getAllTeams,
                                                                       UpdateTeam updateTeam) {
    return new RemoveFirstPlayerFromTeamUseCase(
      getAllGames,
      updateGame,
      getAllTeams,
      updateTeam
    );
  }

  @ApplicationScoped
  public RemoveSecondPlayerFromTeamInput removeSecondPlayerFromTeamInput(GetAllGames getAllGames,
                                                                         UpdateGame updateGame,
                                                                         GetAllTeams getAllTeams,
                                                                         UpdateTeam updateTeam) {
    return new RemoveSecondPlayerFromTeamUseCase(
      getAllGames,
      updateGame,
      getAllTeams,
      updateTeam
    );
  }

  @ApplicationScoped
  public UpdateRankOfPlayerInput updateRankOfPlayerInput(GetAllGames getAllGames,
                                                         UpdateGame updateGame) {
    return new UpdateRankOfPlayerUseCase(
      getAllGames,
      updateGame
    );
  }

  @ApplicationScoped
  public ResetRankOfPlayerInput resetRankOfPlayerInput(GetAllGames getAllGames,
                                                       UpdateGame updateGame) {
    return new ResetRankOfPlayerUseCase(
      getAllGames,
      updateGame
    );
  }
}
