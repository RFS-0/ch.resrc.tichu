package ch.resrc.tichu.use_cases.find_or_create_user.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.find_or_create_user.ports.input.IntendedUserValidationErrors.MUST_NOT_BE_NULL;

public class IntendedUser {

  private Email email;
  private Name name;

  private IntendedUser() {
  }

  private IntendedUser(IntendedUser other) {
    this.email = other.email;
    this.name = other.name;
  }

  private IntendedUser copied(Consumer<IntendedUser> modification) {
    var theCopy = new IntendedUser(this);
    modification.accept(theCopy);
    return theCopy;
  }

  private static Validation<Seq<ValidationError>, IntendedUser> validation() {
    return allOf(
      attribute(x -> x.email, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.name, notNull(MUST_NOT_BE_NULL))
    );
  }

  public static IntendedUser.Builder anIntendedUser() {
    return new Builder();
  }

  public Email email() {
    return email;
  }

  public Name name() {
    return name;
  }

  public static class Builder {

    private final IntendedUser workpiece;

    private Builder() {
      this.workpiece = new IntendedUser();
    }

    private Builder(IntendedUser workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withEmail(Email email) {
      return new Builder(workpiece.copied(but -> but.email = email));
    }

    public Builder withName(Name name) {
      return new Builder(workpiece.copied(but -> but.name = name));
    }

    public Either<Seq<ValidationError>, IntendedUser> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

class IntendedUserValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    Game.class.getName(), "must not be null"
  );
}
