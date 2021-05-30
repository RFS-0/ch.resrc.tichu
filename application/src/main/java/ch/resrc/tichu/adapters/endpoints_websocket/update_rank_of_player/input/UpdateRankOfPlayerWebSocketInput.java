package ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player.input;

import ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player.dto.IntendedPlayerRankUpdateDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.IntendedPlayerRankUpdate;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.UpdateRankOfPlayerInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.IntendedPlayerRankUpdate.anIntendedPlayerRankUpdate;

public class UpdateRankOfPlayerWebSocketInput {

  private final IntendedPlayerRankUpdate intendedPlayerRankUpdate;

  public UpdateRankOfPlayerWebSocketInput(Json json, String message) {
    IntendedPlayerRankUpdateDto intent = json.parse(message, IntendedPlayerRankUpdateDto.class);
    this.intendedPlayerRankUpdate = validatedIntendedPlayerRankUpdate(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedPlayerRankUpdate> validatedIntendedPlayerRankUpdate(IntendedPlayerRankUpdateDto dto) {
    return anIntendedPlayerRankUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withPlayerId(parse(Id.class, dto.playerId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  public UpdateRankOfPlayerInput.Request request() {
    return new UpdateRankOfPlayerInput.Request(intendedPlayerRankUpdate);
  }
}
