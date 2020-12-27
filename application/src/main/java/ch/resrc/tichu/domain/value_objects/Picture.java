package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;

public class Picture extends DomainPrimitive<Picture, String> implements StringValueObject {

  private final String value;

  public Picture(String value) {
    this.value = value;
  }

  public static Picture of(String literal) {
    return new Picture(literal);
  }

  @Override
  protected String getPrimitiveValue() {
    return value;
  }
}
