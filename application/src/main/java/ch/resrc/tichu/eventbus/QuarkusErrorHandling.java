package ch.resrc.tichu.eventbus;

import static ch.resrc.tichu.usecases.errors.UseCaseProblem.INVALID_INPUT_DETECTED;
import static ch.resrc.tichu.usecases.errors.UseCaseProblem.UNAUTHENTICATED;

import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.errorhandling.faults.Fault;
import ch.resrc.tichu.capabilities.errorhandling.faults.OurFault;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.endpoints.errorhandling.ErrorResponse;
import javax.naming.AuthenticationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuarkusErrorHandling implements ExceptionMapper<Exception> {

  private static final Logger LOG = LoggerFactory.getLogger(QuarkusErrorHandling.class);

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  public QuarkusErrorHandling(ProblemCatalogue problemCatalogue, Json json) {
    this.json = json;
    this.problemCatalogue = problemCatalogue;
  }

  @Override
  public Response toResponse(Exception bad) {
    return handleFault(OurFault.of(bad));
  }


  public Response handleFault(Fault bad) {
    var errorPresenter = new ErrorResponse(problemCatalogue, json);
    if (bad instanceof ClientFault) {
      errorPresenter.presentBusinessError((ClientFault) bad);
    } else {
      errorPresenter.presentSystemFailure(bad);
    }
    return errorPresenter.asResponseEntity();
  }

  public Response handleInvalidInputDetected(InvalidInputDetected bad) {
    return handleFault(
      ClientFault.of(ProblemDiagnosis.of(INVALID_INPUT_DETECTED)
        .withContext("validationMessage", bad.getMessage()))
    );
  }

  public Response handleAuthenticationException(AuthenticationException bad) {
    var fault = ClientFault.of(ProblemDiagnosis.of(UNAUTHENTICATED).withCause(bad));
    return handleFault(fault);
  }
}
