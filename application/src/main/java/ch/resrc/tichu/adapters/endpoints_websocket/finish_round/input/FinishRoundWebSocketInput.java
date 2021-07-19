package ch.resrc.tichu.adapters.endpoints_websocket.finish_round.input;

import ch.resrc.tichu.adapters.endpoints_websocket.finish_round.dto.IntendedRoundFinishDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import ch.resrc.tichu.use_cases.finish_round.ports.input.FinishRoundInput;
import ch.resrc.tichu.use_cases.finish_round.ports.input.IntendedRoundFinish;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.finish_round.ports.input.IntendedRoundFinish.anIntendedRoundFinish;

public class FinishRoundWebSocketInput {

  private final IntendedRoundFinish intendedRoundFinish;

  public FinishRoundWebSocketInput(Json json, String message) {
    final var intent = json.parse(message, IntendedRoundFinishDto.class);
    this.intendedRoundFinish = validatedIntendedRoundFinish(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedRoundFinish> validatedIntendedRoundFinish(IntendedRoundFinishDto dto) {
    return anIntendedRoundFinish()
      .withGameId(parse(Id.class, dto.gameId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  public FinishRoundInput.Request request() {
    return new FinishRoundInput.Request(intendedRoundFinish);
  }
}
