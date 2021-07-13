package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.min;

public class RoundNumber {

  private final int value;

  private RoundNumber(int value) {
    this.value = value;
  }

  private static Validation<Seq<ValidationError>, Integer> validation() {
    return allOf(
      min(1, DomainValidationErrors.errorDetails("must not be smaller than one"))
    );
  }

  public static Either<Seq<ValidationError>, RoundNumber> resultOf(int value) {
    return validation().applyTo(value).map(RoundNumber::new);
  }

  public static Either<Seq<ValidationError>, RoundNumber> resultOf(String value) {
    return validation().applyTo(Integer.parseInt(value)).map(RoundNumber::new);
  }

  public RoundNumber increment() {
    return new RoundNumber(value + 1);
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
