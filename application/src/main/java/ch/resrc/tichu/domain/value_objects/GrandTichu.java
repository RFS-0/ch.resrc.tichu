package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;

public class GrandTichu extends DomainPrimitive<GrandTichu, Integer> implements StringValueObject {

  private final Id playerId;
  private final Integer value = 200;

  private GrandTichu(Id playerId) {
    this.playerId = playerId;
  }

  public static GrandTichu of(Id playerId) {
    return new GrandTichu(playerId);
  }

  @Override
  protected Integer getPrimitiveValue() {
    return value;
  }
}
