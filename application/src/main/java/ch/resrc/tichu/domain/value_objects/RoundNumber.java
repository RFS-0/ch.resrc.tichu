package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.min;
import static ch.resrc.tichu.domain.value_objects.RoundNumberValidationErrors.MUST_NOT_BE_SMALLER_THAN_ONE;

public class RoundNumber {

  private final int value;

  private RoundNumber(int value) {
    this.value = value;
  }

  private static Validation<Seq<ValidationError>, Integer> validation() {
    return allOf(
      min(1, MUST_NOT_BE_SMALLER_THAN_ONE)
    );
  }

  public static Either<Seq<ValidationError>, RoundNumber> resultOf(int value) {
    return validation().applyTo(value).map(RoundNumber::new);
  }

  public static Either<Seq<ValidationError>, RoundNumber> resultOf(String value) {
    return validation().applyTo(Integer.parseInt(value)).map(RoundNumber::new);
  }

  public int value() {
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

    RoundNumber that = (RoundNumber) o;

    return value == that.value;
  }

  @Override
  public int hashCode() {
    return value;
  }
}

class RoundNumberValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_SMALLER_THAN_ONE = () -> ValidationError.of(
    Ranks.class.getName(), "must not be smaller than one"
  );

}
