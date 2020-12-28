package ch.resrc.tichu.adapters.websocket.games.output;

import ch.resrc.tichu.adapters.websocket.games.dto.GameDto;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.json.JsonBody;
import ch.resrc.tichu.endpoints.output.WebSocketResponse;
import ch.resrc.tichu.usecases.create_game.ports.documents.GameDocument;
import ch.resrc.tichu.usecases.create_game.ports.outbound.GamePresenter;

public class CreateGameOutput extends WebSocketResponse<GameDto> implements GamePresenter {

  private JsonBody<GameDto> documentPresentation;

  public CreateGameOutput(ProblemCatalogue problemCatalogue, Json json) {
    super(json, problemCatalogue);
  }

  @Override
  public void present(GameDocument toBePresented) {
    final var theDto = GameDto.fromDocument(toBePresented);
    this.documentPresentation = JsonBody.responseBodyOf(theDto, json);
  }

  public JsonBody<GameDto> documentPresentation() {
    return documentPresentation;
  }

  @Override
  protected boolean hasDocumentPresentation() {
    return documentPresentation != null;
  }
}
