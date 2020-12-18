package ch.resrc.tichu.adapters.endpoints_rest.errorhandling;

import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidInputDetectedMapper implements ExceptionMapper<InvalidInputDetected> {

  @Override
  public Response toResponse(InvalidInputDetected exception) {
    return Response.status(Response.Status.BAD_REQUEST)
      .entity(exception.getMessage())
      .build();
  }
}
