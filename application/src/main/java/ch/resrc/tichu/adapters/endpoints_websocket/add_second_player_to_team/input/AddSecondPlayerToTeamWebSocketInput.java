package ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team.input;

import ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team.dto.IntendedFirstPlayerAdditionDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.add_second_player_to_team.ports.input.AddSecondPlayerToTeamInput;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition.anIntendedPlayerAddition;

public class AddSecondPlayerToTeamWebSocketInput {

  private final IntendedPlayerAddition intendedSecondPlayerAddition;

  public AddSecondPlayerToTeamWebSocketInput(Json json, String message) {
    IntendedFirstPlayerAdditionDto intent = json.parse(message, IntendedFirstPlayerAdditionDto.class);
    intendedSecondPlayerAddition = validateIntendedPlayerAddition(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedPlayerAddition> validateIntendedPlayerAddition(IntendedFirstPlayerAdditionDto dto) {
    return anIntendedPlayerAddition()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  public AddSecondPlayerToTeamInput.Request request() {
    return new AddSecondPlayerToTeamInput.Request(intendedSecondPlayerAddition);
  }
}
