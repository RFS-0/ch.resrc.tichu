package ch.resrc.tichu.adapters.endpoints_rest.users.output;

import ch.resrc.tichu.adapters.endpoints_rest.users.dto.UserDto;
import ch.resrc.tichu.capabilities.errorhandling.DomainProblem;
import ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.FindOrCreateUserOutput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.UserDocument;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

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
