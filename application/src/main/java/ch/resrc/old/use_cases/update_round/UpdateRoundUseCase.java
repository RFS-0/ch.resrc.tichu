package ch.resrc.old.use_cases.update_round;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.common.output.*;
import ch.resrc.old.use_cases.update_round.ports.input.*;
import ch.resrc.old.use_cases.update_round.ports.output.*;
import io.vavr.collection.*;

import static ch.resrc.old.capabilities.errorhandling.DomainProblem.*;
import static ch.resrc.old.capabilities.errorhandling.DomainProblemDetected.*;
import static ch.resrc.old.capabilities.errorhandling.PersistenceProblem.*;

public class UpdateRoundUseCase implements UpdateRoundInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;

  public UpdateRoundUseCase(GetAllGames getAllGames, UpdateGame updateGame) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public UpdateRoundOutput.Response apply(Request requested) {
    IntendedRoundUpdate intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToUpdate = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Round roundToUpdate = gameToUpdate.rounds()
      .find(round -> round.roundNumber().equals(intent.roundNumber()))
      .getOrElseThrow(supplierFor(ROUND_NOT_FOUND));

    Round updatedRound = roundToUpdate.finish();

    Game updatedGame = gameToUpdate.butRound(updatedRound);

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new UpdateRoundOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
