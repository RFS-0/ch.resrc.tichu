package ch.resrc.tichu.use_cases.games.create_a_game;

import ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDetected;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.JoinCode;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.input.CreateGameInput;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.input.IntendedGame;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.output.CreateGameOutput;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.output.GameDocument;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.time.Instant;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAMES_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_SAVED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.INVARIANT_VIOLATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.PLAYER_NOT_SAVED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAMS_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.TEAM_NOT_SAVED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.USER_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

public class CreateGameUseCase implements CreateGameInput {

  private final GetAllGames getAllGames;
  private final AddGame addGame;
  private final GetAllTeams getAllTeams;
  private final AddTeam addTeam;
  private final GetAllPlayers getAllPlayers;
  private final AddPlayer addPlayer;
  private final GetAllUsers getAllUsers;

  public CreateGameUseCase(GetAllGames getAllGames,
                           AddGame addGame,
                           GetAllTeams getAllTeams,
                           AddTeam addTeam,
                           GetAllPlayers getAllPlayers,
                           AddPlayer addPlayer,
                           GetAllUsers getAllUsers) {
    this.getAllGames = getAllGames;
    this.addGame = addGame;
    this.getAllTeams = getAllTeams;
    this.addTeam = addTeam;
    this.getAllPlayers = getAllPlayers;
    this.addPlayer = addPlayer;
    this.getAllUsers = getAllUsers;
  }

  @Override
  public CreateGameOutput.Response apply(Request requested) {
    IntendedGame intent = requested.intent();

    Set<User> existingUsers = getAllUsers.getAll().getOrElseThrow(problem(READ_FAILED));

    User createdByUser = existingUsers
      .find(user -> user.id().equals(intent.createdBy()))
      .getOrElseThrow(problem(USER_NOT_FOUND));

    Set<Player> existingPlayers = getAllPlayers.getAll().getOrElseThrow(problem(READ_FAILED));

    Option<Player> existingPlayer = existingPlayers.find(player -> player.id().equals(intent.createdBy()));

    Player createdByPlayer;
    if (existingPlayer.isDefined()) {
      createdByPlayer = existingPlayer.get();
    } else {
      createdByPlayer = Player.create(createdByUser.id(), createdByUser.name(), createdByUser.createdAt()).get();
      addPlayer.add(existingPlayers, createdByPlayer).getOrElseThrow(problem(PLAYER_NOT_SAVED));
    }

    Set<Team> existingTeams = getAllTeams.getAll().getOrElseThrow(problem(TEAMS_NOT_FOUND));

    Team leftTeam = Team.create(Id.next())
      .getOrElseThrow(problem(INVARIANT_VIOLATED))
      .butFirstPlayer(createdByPlayer);
    addTeam.add(existingTeams, leftTeam).getOrElseThrow(problem(TEAM_NOT_SAVED));

    Team rightTeam = Team.create(Id.next()).getOrElseThrow(problem(INVARIANT_VIOLATED));
    addTeam.add(existingTeams, rightTeam).getOrElseThrow(problem(TEAM_NOT_SAVED));

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(problem(GAMES_NOT_FOUND));

    Game game = Game.create(
      Id.next(),
      createdByUser,
      JoinCode.next(),
      HashSet.of(leftTeam, rightTeam),
      HashSet.empty(),
      Instant.now()
    ).getOrElseThrow(problem(INVARIANT_VIOLATED));

    addGame.add(existingGames, game).getOrElseThrow(problem(GAME_NOT_SAVED));

    return new CreateGameOutput.Response(
      GameDocument.fromGame(game)
    );
  }

  private Supplier<ProblemDetected> problem(Problem problem) {
    return () -> DomainProblemDetected.of(problem);
  }
}
