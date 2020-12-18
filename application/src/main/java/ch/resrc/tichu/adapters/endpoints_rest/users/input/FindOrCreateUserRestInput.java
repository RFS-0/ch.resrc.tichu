package ch.resrc.tichu.adapters.endpoints_rest.users.input;

import ch.resrc.tichu.adapters.endpoints_rest.users.dto.IntendedUserDto;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.FindOrCreateUserInput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.IntendedUser;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.find_or_create_user.ports.input.IntendedUser.anIntendedUser;

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
