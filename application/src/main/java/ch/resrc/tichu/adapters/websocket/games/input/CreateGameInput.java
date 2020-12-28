package ch.resrc.tichu.adapters.websocket.games.input;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.origin;
import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.usecases.create_game.ports.documents.IntendedGame.anIntendedGame;

import ch.resrc.tichu.adapters.websocket.games.dto.IntendedGameDto;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validatable;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.endpoints.input.ClientInput;
import ch.resrc.tichu.usecases.create_game.ports.documents.IntendedGame;
import ch.resrc.tichu.usecases.create_game.ports.inbound.CreateGame;

public class CreateGameInput implements Validatable<CreateGameInput>, ClientInput {

  private final IntendedGameDto intendedGameDto;

  public CreateGameInput(IntendedGameDto intendedGameDto) {
    this.intendedGameDto = intendedGameDto;
  }

  @Override
  public Result<CreateGameInput, ValidationError> validated() {
    return validatedIntendedGame().yield(() -> this);
  }

  public CreateGame.Request request() throws InvalidInputDetected {
    return new CreateGame.Request(intendedGame());
  }

  private IntendedGame intendedGame() throws InvalidInputDetected {
    return validatedIntendedGame().getOrThrow(InvalidInputDetected::of);
  }

  private Result<IntendedGame, ValidationError> validatedIntendedGame() {
    return anIntendedGame()
      .withCreatedBy(parse(Id.class, intendedGameDto.userId))
      .buildResult()
      .mapErrors(origin("message"));
  }
}
