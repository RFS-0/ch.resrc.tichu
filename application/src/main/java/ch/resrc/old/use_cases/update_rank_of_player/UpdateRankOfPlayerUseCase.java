package ch.resrc.old.use_cases.update_rank_of_player;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.common.output.*;
import ch.resrc.old.use_cases.update_rank_of_player.ports.input.*;
import ch.resrc.old.use_cases.update_rank_of_player.ports.output.*;
import io.vavr.collection.*;

import static ch.resrc.old.capabilities.errorhandling.DomainProblem.*;
import static ch.resrc.old.capabilities.errorhandling.DomainProblemDetected.*;
import static ch.resrc.old.capabilities.errorhandling.PersistenceProblem.*;

public class UpdateRankOfPlayerUseCase implements UpdateRankOfPlayerInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;

  public UpdateRankOfPlayerUseCase(GetAllGames getAllGames, UpdateGame updateGame) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public UpdateRankOfPlayerOutput.Response apply(Request requested) {
    IntendedPlayerRankUpdate intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToUpdate = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Round roundToUpdate = gameToUpdate.rounds()
      .find(round -> round.roundNumber().equals(intent.roundNumber()))
      .getOrElseThrow(supplierFor(ROUND_NOT_FOUND));

    Ranks updatedRanks = roundToUpdate.ranks().nextRank(intent.playerId())
      .getOrElseThrow(supplierFor(INVARIANT_VIOLATED));

    Round updatedRound = roundToUpdate.butRanks(updatedRanks);

    Game updatedGame = gameToUpdate.butRound(updatedRound);

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new UpdateRankOfPlayerOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
