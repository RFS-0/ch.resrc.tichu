package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.regex.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Email {

  public static final String EMAIL_ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

  private final String value;

  private Email(String literal) {
    value = literal;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return chained(
      notBlank(errorDetails("value must not be blank")),
      matches(EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE, errorDetails("value must be a valid email address"))
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
