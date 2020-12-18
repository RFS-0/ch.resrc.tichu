package ch.resrc.tichu.capabilities.validation;

import io.vavr.collection.List;
import io.vavr.collection.Seq;

import java.util.function.Function;

/**
 * Thrown if some validation finds that a one or more {@link Input}s represent invalid values in a given context.
 */
public class InvalidInputDetected extends RuntimeException {

  private final Seq<ValidationError> validationErrors;

  private InvalidInputDetected(Seq<ValidationError> validationErrors) {
    this.validationErrors = List.ofAll(validationErrors);
  }

  private InvalidInputDetected(InvalidInputDetected other, Throwable cause) {
    super(cause);
    this.validationErrors = List.ofAll(other.validationErrors);
  }

  public static InvalidInputDetected of(ValidationError validationError) {
    return new InvalidInputDetected(List.of(validationError));
  }

  public static InvalidInputDetected of(Seq<ValidationError> validationErrors) {
    return new InvalidInputDetected(validationErrors);
  }

  public InvalidInputDetected withCause(Throwable cause) {
    return new InvalidInputDetected(this, cause);
  }

  public InvalidInputDetected map(Function<ValidationError, ValidationError> mapper) {
    return new InvalidInputDetected(validationErrors).map(mapper);
  }

  public List<ValidationError> errors() {
    return List.ofAll(validationErrors);
  }

  @Override
  public String getMessage() {
    return validationErrors
      .foldLeft(List.empty(), (list, error) -> list.append(error.errorMessage()))
      .toString();
  }
}
