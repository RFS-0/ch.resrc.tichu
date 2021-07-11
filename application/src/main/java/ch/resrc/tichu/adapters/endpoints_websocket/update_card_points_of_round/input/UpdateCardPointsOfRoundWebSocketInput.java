package ch.resrc.tichu.adapters.endpoints_websocket.update_card_points_of_round.input;

import ch.resrc.tichu.adapters.endpoints_websocket.update_card_points_of_round.dto.IntendedCardPointsUpdateDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.IntendedCardPointsUpdate;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.UpdateCardPointsOfRoundInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.IntendedCardPointsUpdate.anIntendedCardPointsUpdate;

public class UpdateCardPointsOfRoundWebSocketInput {

  private final IntendedCardPointsUpdate intendedCardPointsUpdate;

  public UpdateCardPointsOfRoundWebSocketInput(Json json, String message) {
    IntendedCardPointsUpdateDto intent = json.parse(message, IntendedCardPointsUpdateDto.class);
    this.intendedCardPointsUpdate = validatedIntendedTeamName(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedCardPointsUpdate> validatedIntendedTeamName(IntendedCardPointsUpdateDto dto) {
    return anIntendedCardPointsUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .withCardPoints(parse(Integer.class, dto.cardPoints))
      .buildResult();
  }

  public UpdateCardPointsOfRoundInput.Request request() {
    return new UpdateCardPointsOfRoundInput.Request(intendedCardPointsUpdate);
  }
}
