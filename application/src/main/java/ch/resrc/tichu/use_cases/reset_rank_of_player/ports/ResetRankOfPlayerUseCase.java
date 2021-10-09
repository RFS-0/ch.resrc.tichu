package ch.resrc.tichu.use_cases.reset_rank_of_player.ports;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.value_objects.Ranks;
import ch.resrc.tichu.domain.value_objects.Round;
import ch.resrc.tichu.use_cases.common.output.GameDocument;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.IntendedPlayerRankReset;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.ResetRankOfPlayerInput;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.output.ResetRankOfPlayerOutput;
import io.vavr.collection.Set;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.INVARIANT_VIOLATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.ROUND_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

public class ResetRankOfPlayerUseCase implements ResetRankOfPlayerInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;

  public ResetRankOfPlayerUseCase(GetAllGames getAllGames, UpdateGame updateGame) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public ResetRankOfPlayerOutput.Response apply(Request requested) {
    IntendedPlayerRankReset intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToUpdate = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Round roundToUpdate = gameToUpdate.rounds()
      .find(round -> round.roundNumber().equals(intent.roundNumber()))
      .getOrElseThrow(supplierFor(ROUND_NOT_FOUND));

    Ranks updatedRanks = roundToUpdate.ranks().resetRank(intent.playerId())
      .getOrElseThrow(supplierFor(INVARIANT_VIOLATED));

    Round updatedRound = roundToUpdate.butRanks(updatedRanks);

    Game updatedGame = gameToUpdate.butRound(updatedRound);

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new ResetRankOfPlayerOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }
}
