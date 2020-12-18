package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.isUrl;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.value_objects.PictureValidationErrors.MUST_BE_URL;
import static ch.resrc.tichu.domain.value_objects.PictureValidationErrors.MUST_NOT_BE_BLANK;

public class Picture {

  private final String value;

  private Picture(String value) {
    this.value = value;
  }

  public static Validation<Seq<ValidationError>, String> validation() {
    return allOf(
      notBlank(MUST_NOT_BE_BLANK),
      isUrl(MUST_BE_URL)
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

class PictureValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_BLANK = () -> ValidationError.of(
    Picture.class.getName(), "value must not be blank"
  );
  static final Supplier<ValidationError> MUST_BE_URL = () -> ValidationError.of(
    Picture.class.getName(), "value must be a valid URL"
  );
}
