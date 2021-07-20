package ch.resrc.tichu.use_cases.finish_game;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;
import ch.resrc.tichu.use_cases.finish_game.ports.input.FinishGameInput;
import ch.resrc.tichu.use_cases.finish_game.ports.output.FinishGameOutput;
import io.vavr.collection.Set;

import java.time.Instant;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

public class FinishGameUseCase implements FinishGameInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;

  public FinishGameUseCase(GetAllGames getAllGames, UpdateGame updateGame) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public FinishGameOutput.Response apply(Request requested) {
    final var intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToFinish = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Game finishedGame = gameToFinish.butFinishedAt(Instant.now());

    updateGame.update(existingGames, finishedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new FinishGameOutput.Response(
      GameDocument.fromGame(finishedGame)
    );
  }
}
