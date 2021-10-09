package ch.resrc.tichu.configuration;

import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.FindGame;
import ch.resrc.tichu.domain.operations.FindOrCreatePlayerForUser;
import ch.resrc.tichu.domain.operations.FindPlayerByUserId;
import ch.resrc.tichu.domain.operations.FindUser;
import ch.resrc.tichu.domain.operations.FinishGame;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.operations.LoadAllGames;
import ch.resrc.tichu.domain.operations.LoadAllTeams;
import ch.resrc.tichu.domain.operations.SaveGame;
import ch.resrc.tichu.domain.operations.SaveTeams;
import ch.resrc.tichu.domain.operations.UpdateFirstPlayerOfTeam;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdatePlayersOfTeam;
import ch.resrc.tichu.domain.operations.UpdateSecondPlayerOfTeam;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import ch.resrc.tichu.use_cases.add_first_player_to_team.UpdateFirstPlayerOfTeamUseCase;
import ch.resrc.tichu.use_cases.add_second_player_to_team.UpdateSecondPlayerOfTeamUseCase;
import ch.resrc.tichu.use_cases.create_game.CreateGameUseCase;
import ch.resrc.tichu.use_cases.find_or_create_user.FindOrCreateUserUseCase;
import ch.resrc.tichu.use_cases.finish_game.FinishGameUseCase;
import ch.resrc.tichu.use_cases.finish_round.FinishRoundUseCase;
import ch.resrc.tichu.use_cases.finish_round.ports.input.FinishRoundInput;
import ch.resrc.tichu.use_cases.remove_first_player_from_team.RemoveFirstPlayerFromTeamUseCase;
import ch.resrc.tichu.use_cases.remove_first_player_from_team.ports.input.RemoveFirstPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.RemoveSecondPlayerFromTeamUseCase;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.input.RemoveSecondPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.ResetRankOfPlayerUseCase;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.ResetRankOfPlayerInput;
import ch.resrc.tichu.use_cases.update_a_team_name.UpdateTeamNameUseCase;
import ch.resrc.tichu.use_cases.update_card_points_of_round.UpdateCardPointsOfRoundUseCase;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.UpdateCardPointsOfRoundInput;
import ch.resrc.tichu.use_cases.update_rank_of_player.UpdateRankOfPlayerUseCase;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.UpdateRankOfPlayerInput;
import ch.resrc.tichu.use_cases.update_round.UpdateRoundUseCase;
import ch.resrc.tichu.use_cases.update_round.ports.input.UpdateRoundInput;

import javax.enterprise.context.ApplicationScoped;

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
