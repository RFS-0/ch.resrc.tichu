package ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name.input;

import ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name.dto.IntendedTeamNameDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.update_a_team_name.ports.input.IntendedTeamName;
import ch.resrc.tichu.use_cases.update_a_team_name.ports.input.UpdateTeamNameInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.update_a_team_name.ports.input.IntendedTeamName.anIntendedTeamName;

public class UpdateTeamNameWebSocketInput {

  private final IntendedTeamName intendedTeamName;

  public UpdateTeamNameWebSocketInput(Json json, String message) {
    IntendedTeamNameDto intent = json.parse(message, IntendedTeamNameDto.class);
    this.intendedTeamName = validatedIntendedTeamName(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedTeamName> validatedIntendedTeamName(IntendedTeamNameDto dto) {
    return anIntendedTeamName()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withTeamName(parse(Name.class, dto.teamName))
      .buildResult();
  }

  public UpdateTeamNameInput.Request request() {
    return new UpdateTeamNameInput.Request(intendedTeamName);
  }
}
