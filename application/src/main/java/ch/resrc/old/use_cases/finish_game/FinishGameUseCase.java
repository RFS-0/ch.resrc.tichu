package ch.resrc.old.use_cases.finish_game;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.use_cases.common.output.*;
import io.vavr.control.*;

import java.time.*;

public class FinishGameUseCase implements FinishGame {

  private final FindGame findGame;
  private final LoadAllGames loadAllGames;
  private final UpdateGame updateGame;

  public FinishGameUseCase(FindGame findGame,
                           LoadAllGames loadAllGames,
                           UpdateGame updateGame) {
    this.findGame = findGame;
    this.loadAllGames = loadAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public Either<FinishGameProblem, FinishGameOutput> invoke(FinishGameInput input) {
    final var problemOrGameToFinish = findGame.find(input.gameId());

    if (problemOrGameToFinish.isLeft()) {
      return Either.left(FinishGameProblem.operationFailed(problemOrGameToFinish.getLeft()));
    }

    Game finishedGame = problemOrGameToFinish.get().butFinishedAt(Instant.now());

    final var problemOrGames = loadAllGames.loadAll();

    if (problemOrGames.isLeft()) {
      return Either.left(FinishGameProblem.operationFailed(problemOrGames.getLeft()));
    }

    final var problemOrUpdatedGames = updateGame.update(problemOrGames.get(), finishedGame);

    if (problemOrUpdatedGames.isLeft()) {
      return Either.left(FinishGameProblem.operationFailed(problemOrGames.getLeft()));
    }

    return Either.right(new FinishGameOutput(
      GameDocument.fromGame(finishedGame)
    ));
  }
}
