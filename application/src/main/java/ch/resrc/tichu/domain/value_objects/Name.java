package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.errorDetails;

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
