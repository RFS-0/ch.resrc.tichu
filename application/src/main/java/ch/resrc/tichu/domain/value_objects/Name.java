package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.value_objects.NameValidationErrors.MUST_NOT_BE_BLANK;

public class Name {

  private final String value;

  private Name(String literal) {
    value = literal;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return allOf(notBlank(MUST_NOT_BE_BLANK));
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

class NameValidationErrors {
  static final Supplier<ValidationError> MUST_NOT_BE_BLANK = () -> ValidationError.of(
    Name.class.getName(), "value must not be blank"
  );
}
