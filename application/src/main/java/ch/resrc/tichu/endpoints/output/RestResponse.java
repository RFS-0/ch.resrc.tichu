package ch.resrc.tichu.endpoints.output;


import ch.resrc.tichu.capabilities.errorhandling.BusinessError;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import ch.resrc.tichu.endpoints.errorhandling.ErrorResponse;
import javax.ws.rs.core.Response;

/**
 * Base class for all REST responses that act as presenters to use cases. Implements the common error presentation logic. Concrete
 * subclasses take care of the happy case presentation of the response body.
 *
 * @param <T> the DTO type that defines the body of the response.
 * @implNote template method pattern
 */
public abstract class RestResponse<T> implements ErrorPresenter, HavingPresentation, ProvideJsonBodyResponseEntity<T> {

  protected final int successHttpStatus;
  protected final Json json;
  protected final ErrorResponse errorResponse;

  protected RestResponse(int successHttpStatus, ProblemCatalogue problemCatalogue, Json json) {

    this.successHttpStatus = successHttpStatus;
    this.errorResponse = new ErrorResponse(problemCatalogue, json);
    this.json = json;
  }

  protected abstract Response documentPresentation();

  protected abstract boolean hasDocumentPresentation();

  @Override
  public Response asResponseEntity() {
    if (errorResponse.hasErrors()) {
      return errorResponse.asResponseEntity();
    } else {
      return documentPresentation();
    }
  }

  @Override
  public void presentSystemFailure(RuntimeException failure) {
    this.errorResponse.presentSystemFailure(failure);
  }

  @Override
  public void presentBusinessError(BusinessError businessError) {
    this.errorResponse.presentBusinessError(businessError);
  }

  @Override
  public boolean isPresentationMissing() {
    return !hasDocumentPresentation() && !this.errorResponse.hasErrors();
  }

}
