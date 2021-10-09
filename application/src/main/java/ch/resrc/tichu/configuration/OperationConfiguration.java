package ch.resrc.tichu.configuration;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.FindGame;
import ch.resrc.tichu.domain.operations.FindOrCreatePlayerForUser;
import ch.resrc.tichu.domain.operations.FindOrCreatePlayerForUser.FindOrCreatePlayerForUserProblem;
import ch.resrc.tichu.domain.operations.FindPlayerByUserId;
import ch.resrc.tichu.domain.operations.FindUser;
import ch.resrc.tichu.domain.operations.LoadAllGames;
import ch.resrc.tichu.domain.operations.LoadAllPlayers;
import ch.resrc.tichu.domain.operations.LoadAllTeams;
import ch.resrc.tichu.domain.operations.LoadAllUsers;
import ch.resrc.tichu.domain.operations.SavePlayer;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdatePlayersOfTeam;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import io.vavr.control.Option;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;

public class OperationConfiguration {

  @ApplicationScoped
  public FindOrCreatePlayerForUser findOrCreatePlayerForUser(LoadAllPlayers loadAllPlayers, SavePlayer savePlayer) {
    return (Id userId, Name playerName) -> {
      final var problemOrAllPlayers = loadAllPlayers.loadAll()
        .mapLeft(FindOrCreatePlayerForUserProblem::sideEffectProblem);

      if (problemOrAllPlayers.isLeft()) {
        return Either.left(problemOrAllPlayers.getLeft());
      }

      final var allPlayers = problemOrAllPlayers.get();

      Option<Player> existingPlayer = allPlayers.find(player -> player.id().equals(userId));
      if (existingPlayer.isDefined()) {
        return Either.right(existingPlayer.get());
      }

      Player created = Player.create(Id.next(), playerName, Instant.now()).get();
      return savePlayer.save(allPlayers, created)
        .mapLeft(FindOrCreatePlayerForUserProblem::sideEffectProblem)
        .map(problemOrPlayers -> created);
    };
  }

  @ApplicationScoped
  public FindUser findUser(LoadAllUsers loadAllUsers) {
    return (Id userId) -> {
      final var problemOrUsers = loadAllUsers.loadAll();

      if (problemOrUsers.isLeft()) {
        return Either.left(FindUser.FindUserProblem.sideEffectFailed(problemOrUsers.getLeft()));
      }

      final var existingUsers = problemOrUsers.get();

      final var found = existingUsers
        .find(user -> user.id().equals(userId));

      if (found.isEmpty()) {
        return Either.left(FindUser.FindUserProblem.userCouldNotBeFound());
      }

      return Either.right(found.get());
    };
  }

  @ApplicationScoped
  public FindPlayerByUserId findPlayer(LoadAllPlayers loadAllPlayers) {
    return (Id playerId) -> {
      final var problemOrPlayers = loadAllPlayers.loadAll();

      if (problemOrPlayers.isLeft()) {
        return Either.left(FindPlayerByUserId.FindPlayerProblem.sideEffectFailed(problemOrPlayers.getLeft()));
      }

      final var existingPlayers = problemOrPlayers.get();

      final var found = existingPlayers
        .find(user -> user.id().equals(playerId));

      if (found.isEmpty()) {
        return Either.left(FindPlayerByUserId.FindPlayerProblem.playerCouldNotBeFound());
      }

      return Either.right(found.get());
    };
  }

  @ApplicationScoped
  public FindGame findGame(LoadAllGames loadAllGames) {
    return (Id gameId) -> {
      final var problemOrGames = loadAllGames.loadAll();

      if (problemOrGames.isLeft()) {
        return Either.left(FindGame.FindGameProblem.sideEffectFailed(problemOrGames.getLeft()));
      }

      Option<Game> foundGame = problemOrGames.get().find(game -> game.id().equals(gameId));

      if (foundGame.isEmpty()) {
        return Either.left(FindGame.FindGameProblem.gameCouldNotBeFound());
      }

      return Either.right(foundGame.get());
    };
  }

  @ApplicationScoped
  public UpdatePlayersOfTeam addFirstPlayerToTeam(LoadAllGames loadAllGames,
                                                  UpdateGame updateGame,
                                                  LoadAllTeams loadAllTeams,
                                                  UpdateTeam updateTeam) {
    return (Id gameId, Id teamId, Tuple2<Player, Player> players) -> {
      final var allGames = loadAllGames.loadAll()
        .getOrElseThrow(supplierFor(PersistenceProblem.READ_FAILED));

      Option<Game> gameToAddPlayerTo = allGames.find(game -> game.id().equals(gameId));

      Option<Team> teamToAddPlayerTo = gameToAddPlayerTo.get().teams()
        .find(team -> team.id().equals(teamId));

      Team updatedTeam = teamToAddPlayerTo.get();
      if (players._1 != null && players._2 != null) {
        updatedTeam = teamToAddPlayerTo.get()
          .butFirstPlayer(players._1)
          .butSecondPlayer(players._2);
      } else if (players._1 != null) {
        updatedTeam = teamToAddPlayerTo.get()
          .butFirstPlayer(players._1);
      } else if (players._2 != null) {
        updatedTeam = teamToAddPlayerTo.get()
          .butFirstPlayer(players._2);
      }

      final var allTeams = loadAllTeams.loadAll();

      final var problemOrUpdatedTeams = updateTeam.update(allTeams.get(), updatedTeam);

      Game updatedGame = gameToAddPlayerTo.get().butTeam(updatedTeam);

      updateGame.update(allGames, updatedGame);

      return (Either<? extends Problem, Game>) updatedGame;
    };
  }
}
