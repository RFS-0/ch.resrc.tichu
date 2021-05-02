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
import ch.resrc.tichu.use_cases.create_a_game.CreateGameUseCase;
import ch.resrc.tichu.use_cases.find_or_create_user.FindOrCreateUserUseCase;
import ch.resrc.tichu.use_cases.teams.add_player.add_first_player.AddFirstPlayerToTeamUseCase;
import ch.resrc.tichu.use_cases.teams.add_player.add_first_player.ports.inbound.AddFirstPlayerToTeam;
import ch.resrc.tichu.use_cases.teams.add_player.add_second_player.AddSecondPlayerToTeamUseCae;
import ch.resrc.tichu.use_cases.teams.add_player.add_second_player.ports.inbound.AddSecondPlayerToTeam;
import ch.resrc.tichu.use_cases.teams.find_by_id.FindTeamByIdUseCase;
import ch.resrc.tichu.use_cases.teams.find_by_id.ports.inbound.FindTeamById;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_first_player_from_team.RemoveFirstPlayerFromTeamUseCase;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_first_player_from_team.ports.inbound.RemoveFirstPlayerFromTeam;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_second_player_from_team.RemoveSecondPlayerFromTeamUseCase;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_second_player_from_team.ports.inbound.RemoveSecondPlayerFromTeam;
import ch.resrc.tichu.use_cases.update_a_team_name.UpdateTeamNameUseCase;

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
  public FindOrCreateUserUseCase findOrCreateUser(AddUser addUser, GetAllUsers getAllUsers) {
    return new FindOrCreateUserUseCase(
      addUser,
      getAllUsers
    );
  }

  @ApplicationScoped
  public FindTeamById findTeamById() {
    return FindTeamByIdUseCase.create(
      new FindTeamByIdUseCase.OutboundPorts()
    );
  }

  @ApplicationScoped
  public AddFirstPlayerToTeam addFirstPlayerToTeam() {
    return AddFirstPlayerToTeamUseCase.create(
      new AddFirstPlayerToTeamUseCase.OutboundPorts()
    );
  }

  @ApplicationScoped
  public AddSecondPlayerToTeam addSecondPlayerToTeam() {
    return AddSecondPlayerToTeamUseCae.create(
      new AddSecondPlayerToTeamUseCae.OutboundPorts()
    );
  }

  @ApplicationScoped
  public RemoveFirstPlayerFromTeam removeFirstPlayerFromTeam() {
    return RemoveFirstPlayerFromTeamUseCase.create(new RemoveFirstPlayerFromTeamUseCase.OutboundPorts());
  }

  @ApplicationScoped
  public RemoveSecondPlayerFromTeam removeSecondPlayerFromTeam() {
    return RemoveSecondPlayerFromTeamUseCase.create(new RemoveSecondPlayerFromTeamUseCase.OutboundPorts());
  }
}
