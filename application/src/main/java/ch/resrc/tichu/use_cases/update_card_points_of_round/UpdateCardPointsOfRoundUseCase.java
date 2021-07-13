package ch.resrc.tichu.use_cases.update_card_points_of_round;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.value_objects.CardPoints;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Round;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.IntendedCardPointsUpdate;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.UpdateCardPointsOfRoundInput;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.output.UpdateCardPointsOfRoundOutput;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.GAME_NOT_UPDATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.INVARIANT_VIOLATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.ROUND_NOT_FOUND;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;
import static ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem.READ_FAILED;

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
