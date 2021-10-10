package ch.resrc.old.adapters.endpoints_rest.users.input;

import ch.resrc.old.adapters.endpoints_rest.users.dto.*;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.input.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.old.domain.validation.DomainObjectInput.*;
import static ch.resrc.old.use_cases.find_or_create_user.ports.input.IntendedUser.*;

public class FindOrCreateUserRestInput {

  private final IntendedUser intendedUser;

  public FindOrCreateUserRestInput(IntendedUserDto intendedUserDto) {
    this.intendedUser = validatedIntendedUser(intendedUserDto).getOrElseThrow(InvalidInputDetected::of);
  }

  private Either<Seq<ValidationError>, IntendedUser> validatedIntendedUser(IntendedUserDto dto) {
    return anIntendedUser()
      .withName(parse(Name.class, dto.name))
      .withEmail(parse(Email.class, dto.email))
      .buildResult();
  }

  public FindOrCreateUserInput.Request request() throws InvalidInputDetected {
    return new FindOrCreateUserInput.Request(intendedUser);
  }
}
