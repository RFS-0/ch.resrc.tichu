package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class JoinCode {

  public static final String JOIN_CODE_PATTERN = "^[a-zA-Z0-9]{8}$";

  private final String value;

  private JoinCode(String literal) {
    value = literal;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return chained(
      notBlank(errorDetails("value must not be blank")),
      matches(JOIN_CODE_PATTERN, errorDetails("value must consist of eight alphanumeric characters"))
    );
  }

  public static Either<Seq<ValidationError>, JoinCode> resultOf(String literal) {
    return validation().applyTo(literal).map(JoinCode::new);
  }

  public static JoinCode next() {
    return new JoinCode(UUID.randomUUID().toString().split("-")[0]);
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JoinCode joinCode = (JoinCode) o;

    return value.equals(joinCode.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
