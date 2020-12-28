package ch.resrc.tichu.endpoints.output;

import ch.resrc.tichu.adapters.websocket.errorhandling.ErrorMessage;
import ch.resrc.tichu.capabilities.errorhandling.BusinessError;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;

public abstract class WebSocketResponse<T> implements ErrorPresenter, HavingPresentation {

  protected final Json json;
  protected final ErrorMessage errorMessage;

  public WebSocketResponse(Json json, ProblemCatalogue problemCatalogue) {
    this.json = json;
    this.errorMessage = new ErrorMessage(problemCatalogue, json);
  }

  protected abstract boolean hasDocumentPresentation();

  @Override
  public void presentSystemFailure(RuntimeException failure) {

    this.errorMessage.presentSystemFailure(failure);
  }

  @Override
  public void presentBusinessError(BusinessError businessError) {

    this.errorMessage.presentBusinessError(businessError);
  }

  @Override
  public boolean isPresentationMissing() {

    return !hasDocumentPresentation() && !this.errorMessage.hasErrors();
  }
}
