package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;

public class Name extends DomainPrimitive<Name, String> implements StringValueObject {

  private final String value;

  public Name(String literal) {
    value = literal;
  }

  public static Name of(String literal) {
    return new Name(literal);
  }

  @Override
  protected String getPrimitiveValue() {
    return value;
  }
}
