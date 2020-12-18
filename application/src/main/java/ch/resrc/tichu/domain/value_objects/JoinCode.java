package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.matches;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.value_objects.JoinCodeValidationErrors.MUST_CONSIST_OF_EIGHT_ALPHANUMERIC_CHARACTERS;
import static ch.resrc.tichu.domain.value_objects.JoinCodeValidationErrors.MUST_NOT_BE_BLANK;

public class JoinCode {

  public static final String JOIN_CODE_PATTERN = "^[a-zA-Z0-9]{8}$";

  private final String value;

  private JoinCode(String literal) {
    value = literal;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return chained(
      notBlank(MUST_NOT_BE_BLANK),
      matches(JOIN_CODE_PATTERN, MUST_CONSIST_OF_EIGHT_ALPHANUMERIC_CHARACTERS)
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

class JoinCodeValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_BLANK = () -> ValidationError.of(
    JoinCode.class.getName(), "value must not be blank"
  );
  static final Supplier<ValidationError> MUST_CONSIST_OF_EIGHT_ALPHANUMERIC_CHARACTERS = () -> ValidationError.of(
    JoinCode.class.getName(), "value must consist of eight alphanumeric characters"
  );
}
