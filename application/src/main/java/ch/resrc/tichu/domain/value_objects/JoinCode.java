package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;
import java.util.UUID;

public class JoinCode extends DomainPrimitive<JoinCode, String> implements StringValueObject {

  private final String value;

  private JoinCode(String literal) {
    value = literal;
  }

  public static JoinCode of(String literal) {
    return new JoinCode(literal);
  }

  public static JoinCode next() {
    return new JoinCode(UUID.randomUUID().toString().split("-")[0]);
  }

  @Override
  protected String getPrimitiveValue() {
    return value;
  }
}
