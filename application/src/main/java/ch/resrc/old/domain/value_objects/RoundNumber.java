package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;

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
