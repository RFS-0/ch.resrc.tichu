package ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.input;

import ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.IntendedGameDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.input.CreateGameInput;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.input.IntendedGame;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.games.create_a_game.ports.input.IntendedGame.anIntendedGame;

public class CreateGameWebSocketInput {

  private final Json json;
  private final IntendedGame intendedGame;

  public CreateGameWebSocketInput(Json json, String message) {
    this.json = json;
    IntendedGameDto intent = json.parse(message, IntendedGameDto.class);
    this.intendedGame = validatedIntendedGame(intent).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedGame> validatedIntendedGame(IntendedGameDto dto) {
    return anIntendedGame()
      .withCreatedBy(parse(Id.class, dto.createdBy))
      .buildResult();
  }

  public CreateGameInput.Request request() {
    return new CreateGameInput.Request(intendedGame);
  }
}
