package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Picture {

  private final String value;

  private Picture(String value) {
    this.value = value;
  }

  public static Validation<Seq<ValidationError>, String> validation() {
    return allOf(
      notBlank(errorDetails("value must not be blank")),
      isUrl(errorDetails("value must be a valid URL"))
    );
  }

  public static Either<Seq<ValidationError>, Picture> resultOf(String literal) {
    return validation().applyTo(literal).map(Picture::new);
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

    Picture picture = (Picture) o;

    return value.equals(picture.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
