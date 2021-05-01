package ch.resrc.tichu.adapters.endpoints_websocket.teams.update_team_name.input;

import ch.resrc.tichu.adapters.endpoints_websocket.teams.update_team_name.dto.IntendedTeamNameDto;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.teams.update_team_name.ports.input.IntendedTeamName;
import ch.resrc.tichu.use_cases.teams.update_team_name.ports.input.UpdateTeamNameInput;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.teams.update_team_name.ports.input.IntendedTeamName.anIntendedTeamName;

public class UpdateTeamNameWebSocketInput {

  private final IntendedTeamName intendedTeamName;

  public UpdateTeamNameWebSocketInput(IntendedTeamName intendedTeamName) {
    this.intendedTeamName = intendedTeamName;
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
