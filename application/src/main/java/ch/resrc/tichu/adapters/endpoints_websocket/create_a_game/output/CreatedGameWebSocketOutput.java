package ch.resrc.tichu.adapters.endpoints_websocket.create_a_game.output;

import ch.resrc.tichu.adapters.endpoints_websocket.create_a_game.dto.GameDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.CreateGameOutput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblem.INVARIANT_VIOLATED;
import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;

public class CreatedGameWebSocketOutput {

  private final Json json;
  private final Id receiver;
  private final GameDto gameDto;

  public CreatedGameWebSocketOutput(Json json, CreateGameOutput.Response response) {
    this.json = json;
    this.receiver = response.toBePresented().createdBy().id();
    this.gameDto = validateGameDto(response.toBePresented()).getOrElseThrow(supplierFor(INVARIANT_VIOLATED));
  }

  private Either<Seq<ValidationError>, GameDto> validateGameDto(GameDocument gameDocument) {
    return Either.right(GameDto.fromDocument(gameDocument));
  }

  public Id receiver() {
    return receiver;
  }

  public String response() {
    return json.toJsonString(gameDto);
  }
}
