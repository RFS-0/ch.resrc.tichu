package ch.resrc.old.use_cases.create_game;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.common.output.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.time.*;

public class CreateGameUseCase implements CreateGame {

  private final FindPlayerByUserId findPlayerByUserId;
  private final LoadAllGames loadAllGames;
  private final SaveTeams saveTeams;
  private final SaveGame saveGame;
  private final FindUser findUser;
  private final LoadAllTeams loadAllTeams;

  public CreateGameUseCase(FindPlayerByUserId findPlayerByUserId,
                           LoadAllTeams loadAllTeams,
                           SaveTeams saveTeams,
                           LoadAllGames loadAllGames,
                           FindUser findUser,
                           SaveGame saveGame) {
    this.findPlayerByUserId = findPlayerByUserId;
    this.loadAllTeams = loadAllTeams;
    this.saveTeams = saveTeams;
    this.loadAllGames = loadAllGames;
    this.findUser = findUser;
    this.saveGame = saveGame;
  }

  @Override
  public Either<CreateGameProblem, CreateGameOutput> invoke(CreateGameInput input) {

    final var problemOrPlayer = findPlayerByUserId.find(input.createdBy());

    if (problemOrPlayer.isLeft()) {
      return Either.left(CreateGameProblem.operationFailed(problemOrPlayer.getLeft()));
    }

    final var problemOrTeams = loadAllTeams.loadAll();
    if (problemOrTeams.isLeft()) {
      return Either.left(CreateGameProblem.operationFailed(problemOrTeams.getLeft()));
    }

    Team leftTeam = Team.create()
      .butFirstPlayer(problemOrPlayer.get());
    Team rightTeam = Team.create();

    final var problemOrUpdatedTeams = saveTeams.save(problemOrTeams.get(), List.of(leftTeam, rightTeam));

    if (problemOrUpdatedTeams.isLeft()) {
      return Either.left(CreateGameProblem.operationFailed(problemOrUpdatedTeams.getLeft()));
    }

    final var problemOrGames = loadAllGames.loadAll();
    if (problemOrGames.isLeft()) {
      return Either.left(CreateGameProblem.operationFailed(problemOrGames.getLeft()));
    }

    final var problemOrUser = findUser.find(input.createdBy());
    if (problemOrUser.isLeft()) {
      return Either.left(CreateGameProblem.operationFailed(problemOrUser.getLeft()));
    }

    final var problemOrGame = Game.create(
      Id.next(),
      problemOrUser.get(),
      JoinCode.next(),
      HashSet.of(leftTeam, rightTeam),
      HashSet.empty(),
      Instant.now()
    );
    if (problemOrGame.isLeft()) {
      return Either.left(CreateGameProblem.validationFailed(problemOrGame.getLeft()));
    }

    final var problemOrSavedGame = saveGame.save(problemOrGames.get(), problemOrGame.get());

    if (problemOrSavedGame.isLeft()) {
      return Either.left(CreateGameProblem.operationFailed(problemOrSavedGame.getLeft()));
    }

    return Either.right(
      new CreateGameOutput(
        GameDocument.fromGame(problemOrGame.get())
      )
    );
  }
}
