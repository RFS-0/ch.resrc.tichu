package ch.resrc.old.use_cases.update_card_points_of_round;

import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.common.output.*;
import ch.resrc.old.use_cases.update_card_points_of_round.ports.input.*;
import ch.resrc.old.use_cases.update_card_points_of_round.ports.output.*;
import io.vavr.collection.*;

import static ch.resrc.old.capabilities.errorhandling.DomainProblem.*;
import static ch.resrc.old.capabilities.errorhandling.DomainProblemDetected.*;
import static ch.resrc.old.capabilities.errorhandling.PersistenceProblem.*;

public class UpdateCardPointsOfRoundUseCase implements UpdateCardPointsOfRoundInput {

  private final GetAllGames getAllGames;
  private final UpdateGame updateGame;

  public UpdateCardPointsOfRoundUseCase(GetAllGames getAllGames, UpdateGame updateGame) {
    this.getAllGames = getAllGames;
    this.updateGame = updateGame;
  }

  @Override
  public UpdateCardPointsOfRoundOutput.Response apply(Request requested) {
    IntendedCardPointsUpdate intent = requested.intent();

    Set<Game> existingGames = getAllGames.getAll().getOrElseThrow(supplierFor(READ_FAILED));

    Game gameToUpdate = existingGames
      .find(game -> game.id().equals(intent.gameId()))
      .getOrElseThrow(supplierFor(GAME_NOT_FOUND));

    Round roundToUpdate = gameToUpdate.rounds()
      .find(round -> round.roundNumber().equals(intent.roundNumber()))
      .getOrElseThrow(supplierFor(ROUND_NOT_FOUND));

    Team theTeam = gameToUpdate.teams()
      .find(team -> team.id() == intent.teamId())
      .getOrElseThrow(supplierFor(INVARIANT_VIOLATED));
    Team otherTeam = gameToUpdate.teams()
      .find(team -> team.id() != intent.teamId())
      .getOrElseThrow(supplierFor(INVARIANT_VIOLATED));

    int pointsOfTheTeam = mapToValidRange(intent.cardPoints());
    int pointsOfOtherTeam = 100 - pointsOfTheTeam;

    Map<Id, Integer> updatedCardPoints = roundToUpdate.cardPoints()
      .values()
      .put(theTeam.id(), pointsOfTheTeam)
      .put(otherTeam.id(), pointsOfOtherTeam);

    Round updatedRound = roundToUpdate.butCardPoints(CardPoints.resultOf(updatedCardPoints).get());

    Game updatedGame = gameToUpdate.butRound(updatedRound);

    updateGame.update(existingGames, updatedGame).getOrElseThrow(supplierFor(GAME_NOT_UPDATED));

    return new UpdateCardPointsOfRoundOutput.Response(
      GameDocument.fromGame(updatedGame)
    );
  }

  private int mapToValidRange(int cardPoints) {
    return Math.max(Math.min(cardPoints, 100), -25);
  }
}
