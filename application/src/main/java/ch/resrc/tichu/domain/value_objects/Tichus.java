package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.Objects;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.isFalseOrError;
import static ch.resrc.tichu.capabilities.validation.Validations.max;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.errorDetails;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

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
