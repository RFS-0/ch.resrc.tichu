package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Tichus {

  private final Map<Id, Tichu> values;

  public Tichus(Map<Id, Tichu> values) {
    this.values = values;
  }

  private static Validation<Seq<ValidationError>, Map<Id, Tichu>> validation() {
    return allOf(
      attribute(x -> x.keySet().length(), max(4, errorDetails("must not be defined more than once per player"))),
      attribute(Map::values, isFalseOrError(tichus -> tichus.exists(tichu -> tichu == Tichu.UNKNOWN), errorDetails("must not contain unknown values")))
    );
  }

  public static Either<Seq<ValidationError>, Tichus> resultOf(Map<Id, Tichu> values) {
    if (values == null || values.keySet().exists(Objects::isNull) || values.values().exists(Objects::isNull)) {
      return Either.left(List.of(mustNotBeNull().apply(values)));
    }
    return validation().applyTo(values).map(Tichus::new);
  }

  public Map<Id, Tichu> values() {
    return values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Tichus tichus = (Tichus) o;

    return values.equals(tichus.values);
  }

  @Override
  public int hashCode() {
    return values.hashCode();
  }
}
