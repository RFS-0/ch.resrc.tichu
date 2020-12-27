package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;

public class RoundNumber extends DomainPrimitive<RoundNumber, Integer> implements StringValueObject {

  private final Integer value;

  private RoundNumber(Integer value) {
    this.value = value;
  }

  public static RoundNumber of(Integer number) {
    return new RoundNumber(number);
  }

  @Override
  protected Integer getPrimitiveValue() {
    return value;
  }
}
