package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.isFalseOrError;
import static ch.resrc.tichu.capabilities.validation.Validations.max;
import static ch.resrc.tichu.domain.value_objects.TichusValidationErrors.MUST_NOT_BE_DEFINED_MORE_THAN_ONCE_PER_PLAYER;
import static ch.resrc.tichu.domain.value_objects.TichusValidationErrors.MUST_NOT_BE_NULL;
import static ch.resrc.tichu.domain.value_objects.TichusValidationErrors.MUST_NOT_CONTAIN_UNKNOWN_VALUES;

public class Tichus {

  private final Map<Id, Tichu> values;

  public Tichus(Map<Id, Tichu> values) {
    this.values = values;
  }

  private static Validation<Seq<ValidationError>, Map<Id, Tichu>> validation() {
    return allOf(
      attribute(x -> x.keySet().length(), max(4, MUST_NOT_BE_DEFINED_MORE_THAN_ONCE_PER_PLAYER)),
      attribute(Map::values, isFalseOrError(tichus -> tichus.exists(tichu -> tichu == Tichu.UNKNOWN), MUST_NOT_CONTAIN_UNKNOWN_VALUES))
    );
  }

  public static Either<Seq<ValidationError>, Tichus> resultOf(Map<Id, Tichu> values) {
    if (values == null || values.keySet().exists(Objects::isNull) || values.values().exists(Objects::isNull)) {
      return Either.left(List.of(MUST_NOT_BE_NULL.get()));
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

class TichusValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    Ranks.class.getName(), "no null values allowed"
  );

  static final Supplier<ValidationError> MUST_NOT_BE_DEFINED_MORE_THAN_ONCE_PER_PLAYER = () -> ValidationError.of(
    Ranks.class.getName(), "must not be defined more than once per player"
  );

  static final Supplier<ValidationError> MUST_NOT_CONTAIN_UNKNOWN_VALUES = () -> ValidationError.of(
    Ranks.class.getName(), "must not contain unknown values"
  );

}
