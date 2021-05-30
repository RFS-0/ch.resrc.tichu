package ch.resrc.tichu.adapters.endpoints_websocket.reset_rank_of_player.input;

import ch.resrc.tichu.adapters.endpoints_websocket.reset_rank_of_player.dto.IntendedPlayerRankResetDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.IntendedPlayerRankReset;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.ResetRankOfPlayerInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.IntendedPlayerRankReset.anIntendedPlayerRankReset;

public class ResetRankOfPlayerWebSocketInput {

  private final IntendedPlayerRankReset intendedPlayerRankReset;

  public ResetRankOfPlayerWebSocketInput(Json json, String message) {
    IntendedPlayerRankResetDto intent = json.parse(message, IntendedPlayerRankResetDto.class);
    this.intendedPlayerRankReset = validatedIntendedPlayerRankReset(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedPlayerRankReset> validatedIntendedPlayerRankReset(IntendedPlayerRankResetDto dto) {
    return anIntendedPlayerRankReset()
      .withGameId(parse(Id.class, dto.gameId))
      .withPlayerId(parse(Id.class, dto.playerId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  public ResetRankOfPlayerInput.Request request() {
    return new ResetRankOfPlayerInput.Request(intendedPlayerRankReset);
  }
}
