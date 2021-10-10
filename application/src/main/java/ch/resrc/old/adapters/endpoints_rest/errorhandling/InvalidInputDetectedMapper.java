package ch.resrc.old.adapters.endpoints_rest.errorhandling;

import ch.resrc.old.capabilities.validations.old.*;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

@Provider
public class InvalidInputDetectedMapper implements ExceptionMapper<InvalidInputDetected> {

  @Override
  public Response toResponse(InvalidInputDetected exception) {
    return Response.status(Response.Status.BAD_REQUEST)
      .entity(exception.getMessage())
      .build();
  }
}
