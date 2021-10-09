package ch.resrc.tichu.use_cases.finish_round;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.value_objects.Round;
import ch.resrc.tichu.use_cases.common.output.GameDocument;
import ch.resrc.tichu.use_cases.finish_round.ports.input.FinishRoundInput;
import ch.resrc.tichu.use_cases.finish_round.ports.input.IntendedRoundFinish;
import ch.resrc.tichu.use_cases.finish_round.ports.output.FinishRoundOutput;
import io.vavr.collection.Set;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.ROUND_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

public class FinishRoundUseCase implements FinishRoundInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;

  public FinishRoundUseCase(GetAllGames getAllGames, UpdateGame updateGame) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public FinishRoundOutput.Response apply(Request requested) {
    IntendedRoundFinish intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToUpdate = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Round roundToUpdate = gameToUpdate.rounds()
      .find(round -> round.roundNumber().equals(intent.roundNumber()))
      .getOrElseThrow(supplierFor(ROUND_NOT_FOUND));

    Round updatedRound = roundToUpdate.finish();

    Game updatedGame = gameToUpdate.butRound(updatedRound);

    if (updatedGame.notFinished()) {
      updatedGame = gameToUpdate.addRound(intent.roundNumber().increment());
    }

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new FinishRoundOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
