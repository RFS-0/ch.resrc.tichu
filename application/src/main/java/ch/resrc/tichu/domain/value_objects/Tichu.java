package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Tichu extends DomainPrimitive<Tichu, Integer> implements StringValueObject {

  private final Id playerId;
  private final boolean successful;
  private final Integer value = 100;

  private Tichu(Id playerId, boolean successful) {
    this.playerId = playerId;
    this.successful = successful;
  }

  public static Tichu of(Id playerId, boolean successful) {
    return new Tichu(playerId, successful);
  }

  public static Tichu called(Id palyerId) {
    return new Tichu(palyerId, false);
  }

  public static Tichu failed(Id playerId) {
    return new Tichu(playerId, false);
  }

  public static Tichu success(Id playerId) {
    return new Tichu(playerId, true);
  }

  @Override
  protected Integer getPrimitiveValue() {
    return successful ? value : -value;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
      .append("playerId", playerId)
      .append("successful", successful)
      .append("value", value)
      .toString();
  }
}
