package ch.resrc.tichu.adapters.endpoints_websocket.update_round.input;

import ch.resrc.tichu.adapters.endpoints_websocket.finish_round.dto.IntendedRoundFinishDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import ch.resrc.tichu.use_cases.update_round.ports.input.IntendedRoundUpdate;
import ch.resrc.tichu.use_cases.update_round.ports.input.UpdateRoundInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.update_round.ports.input.IntendedRoundUpdate.anIntendedRoundUpdate;

public class UpdateRoundWebSocketInput {

  private final IntendedRoundUpdate intendedPlayerRankUpdate;

  public UpdateRoundWebSocketInput(Json json, String message) {
    final var intent = json.parse(message, IntendedRoundFinishDto.class);
    this.intendedPlayerRankUpdate = validatedIntendedRoundUpdate(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedRoundUpdate> validatedIntendedRoundUpdate(IntendedRoundFinishDto dto) {
    return anIntendedRoundUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  public UpdateRoundInput.Request request() {
    return new UpdateRoundInput.Request(intendedPlayerRankUpdate);
  }
}
