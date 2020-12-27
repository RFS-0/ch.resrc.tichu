package ch.resrc.tichu.adapters.websocket.errorhandling;

import static ch.resrc.tichu.capabilities.functional.PersistentCollections.addedTo;
import static ch.resrc.tichu.capabilities.functional.Predicates.satisfies;
import static ch.resrc.tichu.endpoints.errorhandling.RestProblem.HTTP_METHOD_NOT_SUPPORTED;
import static ch.resrc.tichu.endpoints.errorhandling.RestProblem.RESOURCE_NOT_FOUND;
import static ch.resrc.tichu.usecases.errors.UseCaseProblem.UNAUTHENTICATED;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static org.apache.commons.lang3.StringUtils.leftPad;

import ch.resrc.tichu.capabilities.errorhandling.BusinessError;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDetected;
import ch.resrc.tichu.capabilities.errorhandling.faults.OurFault;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.json.JsonBody;
import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import ch.resrc.tichu.endpoints.errorhandling.ErrorDto;
import ch.resrc.tichu.endpoints.errorhandling.WebSocketProblem;
import ch.resrc.tichu.endpoints.output.HavingPresentation;
import ch.resrc.tichu.usecases.errors.UseCaseProblem;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public class ErrorMessage implements ErrorPresenter, HavingPresentation {

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  private ReportedErrors reportedErrors = new NoErrors();

  public ErrorMessage(ProblemCatalogue problemCatalogue, Json json) {
    this.problemCatalogue = problemCatalogue;
    this.json = json;
  }

  public boolean hasErrors() {
    return this.reportedErrors.hasErrors();
  }

  @Override
  public void presentSystemFailure(RuntimeException bad) {
    this.reportedErrors = this.reportedErrors.systemFailureReported(bad);
  }

  @Override
  public void presentBusinessError(BusinessError businessError) {
    this.reportedErrors = this.reportedErrors.businessErrorReported(businessError);
  }

  @Override
  public boolean isPresentationMissing() {
    return false;
  }

  static class WebSocketError {

    ErrorDto.Error errorBody;
  }

  private WebSocketError errorMessage(ProblemDetected fault) {
    var problemCode = problemCatalogue.codeFor(fault.problem());
    var type = "TICHU-" + (leftPad(problemCode.toString(), 4, "0"));

    var errorDto = new ErrorDto.Error();
    errorDto.setType(type)
      .setTitle(fault.title())
      .setDetails(fault.details())
      .setInstance(fault.id().toString())
      .setOccurredOn(OffsetDateTime.ofInstant(fault.occurredOn(), ZoneId.systemDefault()).toString());

    return new WebSocketError() {{
      errorBody = errorDto;
    }};
  }

  private <T> JsonBody<T> asJsonBody(List<WebSocketError> errors) {
    var errorDto = new ErrorDto();
    errors.forEach(x -> errorDto.addError(x.errorBody));

    return JsonBody.errorBodyOf(errorDto, json);
  }

  ///// State machine /////

  private interface ReportedErrors {

    ReportedErrors systemFailureReported(RuntimeException failure);

    ReportedErrors businessErrorReported(BusinessError businessError);

    JsonBody<ErrorDto> asResponseMessage();

    boolean hasErrors();
  }

  private class SystemFailures implements ReportedErrors {

    private List<WebSocketError> errors = List.of();

    SystemFailures(RuntimeException failure) {
      this.systemFailureReported(failure);
    }

    @Override
    public ReportedErrors systemFailureReported(RuntimeException failure) {

      WebSocketError error = errorMessage(OurFault.of(failure));

      this.errors = addedTo(this.errors, error);

      return this;
    }

    @Override
    public ErrorMessage.ReportedErrors businessErrorReported(BusinessError businessError) {
      // Ignored. System failures override any business errors.
      // Business errors are not presented if system failures have occurred.
      return this;
    }

    @Override
    public boolean hasErrors() {
      return !errors.isEmpty();
    }

    @Override
    public JsonBody<ErrorDto> asResponseMessage() {
      WebSocketError reportedError = errors.get(0);
      return asJsonBody(List.of(reportedError));
    }
  }

  private class BusinessErrors implements ReportedErrors {

    private List<WebSocketError> errors = List.of();

    BusinessErrors(BusinessError businessError) {
      this.businessErrorReported(businessError);
    }

    @Override
    public ErrorMessage.ReportedErrors systemFailureReported(RuntimeException failure) {
      return new SystemFailures(failure);
    }

    @Override
    public ErrorMessage.ReportedErrors businessErrorReported(BusinessError businessError) {
      this.errors = addedTo(this.errors, this.clientFaultMessage(businessError));

      return this;
    }

    private WebSocketError clientFaultMessage(BusinessError businessError) {
      ProblemDetected fault = businessError.asException();

      return Match(fault.problem()).of(
        Case($(satisfies(WebSocketProblem.class, problem -> problem.is(RESOURCE_NOT_FOUND))),
          errorMessage(fault)
        ),
        Case($(satisfies(WebSocketProblem.class, problem -> problem.is(HTTP_METHOD_NOT_SUPPORTED))),
          errorMessage(fault)
        ),
        Case($(satisfies(UseCaseProblem.class, problem -> problem.is(UNAUTHENTICATED))),
          errorMessage(fault)
        ),
        Case($(), errorMessage(fault))
      );
    }

    @Override
    public boolean hasErrors() {
      return !errors.isEmpty();
    }

    @Override
    public JsonBody<ErrorDto> asResponseMessage() {
      WebSocketError reportedError = errors.get(0);
      return asJsonBody(List.of(reportedError));
    }
  }

  private class NoErrors implements ReportedErrors {

    @Override
    public ReportedErrors systemFailureReported(RuntimeException failure) {
      return new SystemFailures(failure);
    }

    @Override
    public ReportedErrors businessErrorReported(BusinessError businessError) {
      return new BusinessErrors(businessError);
    }

    @Override
    public boolean hasErrors() {
      return false;
    }

    @Override
    public JsonBody<ErrorDto> asResponseMessage() {
      return asJsonBody(List.of());
    }
  }
}
