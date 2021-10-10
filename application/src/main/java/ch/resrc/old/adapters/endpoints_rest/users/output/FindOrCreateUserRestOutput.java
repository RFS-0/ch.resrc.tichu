package ch.resrc.old.adapters.endpoints_rest.users.output;

import ch.resrc.old.adapters.endpoints_rest.users.dto.*;
import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;
import io.vavr.collection.*;
import io.vavr.control.*;

public class FindOrCreateUserRestOutput {

  private final UserDto userDto;

  public FindOrCreateUserRestOutput(FindOrCreateUserOutput.Response response) {
    this.userDto = validatedUserDto(response.toBePresented()).getOrElseThrow(
      () -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED)
    );
  }

  private Either<Seq<ValidationError>, UserDto> validatedUserDto(UserDocument userDocument) {
    return Either.right(UserDto.fromDocument(userDocument));
  }

  public javax.ws.rs.core.Response response() {
    return javax.ws.rs.core.Response
      .status(javax.ws.rs.core.Response.Status.CREATED)
      .entity(userDto)
      .build();
  }
}