package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;

public class Email extends DomainPrimitive<Email, String> implements StringValueObject {

  private final String value;

  private Email(String literal) {
    value = literal;
  }

  public static Email of(String literal) {
    return new Email(literal);
  }

  @Override
  protected String getPrimitiveValue() {
    return value;
  }
}
