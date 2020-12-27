package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;

public class CardPoints extends DomainPrimitive<CardPoints, Integer> implements StringValueObject {

  private final Id teamId;
  private final Integer value;

  private CardPoints(Id teamId, Integer value) {
    this.teamId = teamId;
    this.value = value;
  }

  public static CardPoints of(Id teamId, Integer points) {
    return new CardPoints(teamId, points);
  }

  @Override
  protected Integer getPrimitiveValue() {
    return value;
  }
}
