package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Name {

  private final String value;

  private Name(String literal) {
    value = literal;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return allOf(notBlank(errorDetails("value must not be blank")));
  }

  public static Either<Seq<ValidationError>, Name> resultOf(String literal) {
    return validation().applyTo(literal).map(Name::new);
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Name name = (Name) o;

    return value.equals(name.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
