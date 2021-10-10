package ch.resrc.old.use_cases.find_or_create_user.ports.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class IntendedUser {

  private Email email;
  private Name name;

  private IntendedUser() {
  }

  private IntendedUser(IntendedUser other) {
    this.email = other.email;
    this.name = other.name;
  }

  public static IntendedUser.Builder anIntendedUser() {
    return new Builder();
  }

  private IntendedUser copied(Consumer<IntendedUser> modification) {
    var theCopy = new IntendedUser(this);
    modification.accept(theCopy);
    return theCopy;
  }

  private static Validation<Seq<ValidationError>, IntendedUser> validation() {
    return allOf(
      attribute(x -> x.email, notNull(mustNotBeNull())),
      attribute(x -> x.name, notNull(mustNotBeNull()))
    );
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

