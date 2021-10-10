package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Name extends DomainPrimitive<Name, String> implements StringValueObject, Comparable<Name> {

  private final String value;

  private Name(String literal) {
    value = literal;
  }

  private static Validation<String, ValidationError> validation() {
    return notBlank();
  }

  public static Result<Name, ValidationError> resultOf(String literal) {
    return validation().applyTo(literal).map(Name::new);
  }

  @Override
  protected String getPrimitiveValue() {
    return value;
  }

  @Override
  public int compareTo(Name other) {
    return this.value.compareTo(other.value);
  }
}
