package ch.resrc.tichu.adapters.endpoints_websocket.finish_game.input;

import ch.resrc.tichu.adapters.endpoints_websocket.finish_game.dto.IntendedGameFinishDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.finish_game.ports.input.FinishGameInput;
import ch.resrc.tichu.use_cases.finish_game.ports.input.IntendedGameFinish;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.finish_game.ports.input.IntendedGameFinish.anIntendedGameFinish;

public class FinishGameWebSocketInput {

  private final IntendedGameFinish intendedGameFinish;

  public FinishGameWebSocketInput(Json json, String message) {
    final var intent = json.parse(message, IntendedGameFinishDto.class);
    this.intendedGameFinish = validatedIntendedGameFinish(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedGameFinish> validatedIntendedGameFinish(IntendedGameFinishDto dto) {
    return anIntendedGameFinish()
      .withGameId(parse(Id.class, dto.gameId()))
      .buildResult();
  }

  public FinishGameInput.Request request() {
    return new FinishGameInput.Request(intendedGameFinish);
  }
}
