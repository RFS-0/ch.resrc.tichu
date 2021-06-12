package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.isUrl;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.errorDetails;

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
