package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.matches;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.value_objects.EmailValidationErrors.MUST_BE_VALID_EMAIL_ADDRESS;
import static ch.resrc.tichu.domain.value_objects.EmailValidationErrors.MUST_NOT_BE_BLANK;

public class Email {

  public static final String EMAIL_ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

  private final String value;

  private Email(String literal) {
    value = literal;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return chained(
      notBlank(MUST_NOT_BE_BLANK),
      matches(EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE, MUST_BE_VALID_EMAIL_ADDRESS)
    );
  }

  public static Either<Seq<ValidationError>, Email> resultOf(String literal) {
    return validation().applyTo(literal).map(Email::new);
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

    Email email = (Email) o;

    return value.equals(email.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}

class EmailValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_BLANK = () -> ValidationError
    .of(Email.class.getName(), "value must not be blank");
  static final Supplier<ValidationError> MUST_BE_VALID_EMAIL_ADDRESS = () -> ValidationError
    .of(Email.class.getName(), "value must be a valid email address");
}
