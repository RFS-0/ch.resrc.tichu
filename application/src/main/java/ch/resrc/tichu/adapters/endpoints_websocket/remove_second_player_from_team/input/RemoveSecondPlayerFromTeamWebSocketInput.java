package ch.resrc.tichu.adapters.endpoints_websocket.remove_second_player_from_team.input;

import ch.resrc.tichu.adapters.endpoints_websocket.remove_second_player_from_team.dto.IntendedSecondPlayerRemovalDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerRemoval;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.input.RemoveSecondPlayerFromTeamInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerRemoval.anIntendedPlayerRemoval;

public class RemoveSecondPlayerFromTeamWebSocketInput {

  private final IntendedPlayerRemoval intendedPlayerRemoval;

  public RemoveSecondPlayerFromTeamWebSocketInput(Json json, String message) {
    IntendedSecondPlayerRemovalDto intent = json.parse(message, IntendedSecondPlayerRemovalDto.class);
    intendedPlayerRemoval = validateIntendedPlayerRemoval(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedPlayerRemoval> validateIntendedPlayerRemoval(IntendedSecondPlayerRemovalDto dto) {
    return anIntendedPlayerRemoval()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  public RemoveSecondPlayerFromTeamInput.Request request() {
    return new RemoveSecondPlayerFromTeamInput.Request(intendedPlayerRemoval);
  }
}
