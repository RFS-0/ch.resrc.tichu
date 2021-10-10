package ch.resrc.old.configuration;

import ch.resrc.old.domain.operations.*;
import ch.resrc.old.use_cases.add_first_player_to_team.*;
import ch.resrc.old.use_cases.add_second_player_to_team.*;
import ch.resrc.old.use_cases.create_game.*;
import ch.resrc.old.use_cases.find_or_create_user.*;
import ch.resrc.old.use_cases.finish_game.*;
import ch.resrc.old.use_cases.finish_round.*;
import ch.resrc.old.use_cases.finish_round.ports.input.*;
import ch.resrc.old.use_cases.remove_first_player_from_team.*;
import ch.resrc.old.use_cases.remove_first_player_from_team.ports.input.*;
import ch.resrc.old.use_cases.remove_second_player_from_team.*;
import ch.resrc.old.use_cases.remove_second_player_from_team.ports.input.*;
import ch.resrc.old.use_cases.reset_rank_of_player.ports.*;
import ch.resrc.old.use_cases.reset_rank_of_player.ports.input.*;
import ch.resrc.old.use_cases.update_a_team_name.*;
import ch.resrc.old.use_cases.update_card_points_of_round.*;
import ch.resrc.old.use_cases.update_card_points_of_round.ports.input.*;
import ch.resrc.old.use_cases.update_rank_of_player.*;
import ch.resrc.old.use_cases.update_rank_of_player.ports.input.*;
import ch.resrc.old.use_cases.update_round.*;
import ch.resrc.old.use_cases.update_round.ports.input.*;

import javax.enterprise.context.*;

public final class UseCasePortsConfiguration {

  @ApplicationScoped
  public CreateGameUseCase createGame(FindPlayerByUserId findPlayerByUserId,
                                      LoadAllTeams loadAllTeams,
                                      SaveTeams saveTeams,
                                      LoadAllGames loadAllGames,
                                      FindUser findUser,
                                      SaveGame saveGame) {
    return new CreateGameUseCase(
      findPlayerByUserId,
      loadAllTeams,
      saveTeams,
      loadAllGames,
      findUser,
      saveGame
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
  public UpdateFirstPlayerOfTeam updateFirstPlayerOfTeam(FindOrCreatePlayerForUser findOrCreatePlayerForUser,
                                                         UpdatePlayersOfTeam updatePlayersOfTeam) {
    return new UpdateFirstPlayerOfTeamUseCase(
      findOrCreatePlayerForUser,
      updatePlayersOfTeam
    );
  }

  @ApplicationScoped
  public UpdateSecondPlayerOfTeam updateSecondPlayerOfTeam(FindOrCreatePlayerForUser findOrCreatePlayerForUser,
                                                           UpdatePlayersOfTeam updatePlayersOfTeam) {
    return new UpdateSecondPlayerOfTeamUseCase(
      findOrCreatePlayerForUser,
      updatePlayersOfTeam
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

  @ApplicationScoped
  public UpdateCardPointsOfRoundInput updateCardPointsOfRoundInput(GetAllGames getAllGames,
                                                                   UpdateGame updateGame) {
    return new UpdateCardPointsOfRoundUseCase(
      getAllGames,
      updateGame
    );
  }

  @ApplicationScoped
  public FinishRoundInput finishRoundInput(GetAllGames getAllGames,
                                           UpdateGame updateGame) {
    return new FinishRoundUseCase(
      getAllGames,
      updateGame
    );
  }

  @ApplicationScoped
  public UpdateRoundInput updateRoundInput(GetAllGames getAllGames,
                                           UpdateGame updateGame) {
    return new UpdateRoundUseCase(
      getAllGames,
      updateGame
    );
  }

  @ApplicationScoped
  public FinishGame finishGameInput(FindGame findGame,
                                    LoadAllGames loadAllGames,
                                    UpdateGame updateGame) {
    return new FinishGameUseCase(
      findGame,
      loadAllGames,
      updateGame
    );
  }
}
