package ch.resrc.tichu.domain.validation;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.HashSet;

import java.util.function.Function;

public final class DomainValidationErrors {

  public static <T> Function<? super T, ? extends ValidationError> mustNotBeNull() {
    return (T toBeValidated) -> ValidationError.of(
      ValidationError.Origin.DOMAIN,
      "domain attribute must not be null",
      toBeValidated,
      HashSet.of(ValidationError.Claim.CAN_BE_DISPLAYED)
    );
  }

  public static <T> Function<T, ValidationError> mustNotBeBlank() {
    return (T toBeValidated) -> ValidationError.of(
      ValidationError.Origin.DOMAIN,
      "domain attribute must not be blank",
      toBeValidated,
      HashSet.of(ValidationError.Claim.CAN_BE_DISPLAYED)
    );
  }

  public static <T> Function<T, ValidationError> mustBeUuid() {
    return (T toBeValidated) -> ValidationError.of(
      ValidationError.Origin.DOMAIN,
      "domain attribute must be uuid",
      toBeValidated,
      HashSet.of(ValidationError.Claim.CAN_BE_DISPLAYED)
    );
  }

  public static <T> Function<T, ValidationError> errorDetails(String details) {
    return (T toBeValidated) -> ValidationError.of(
      ValidationError.Origin.DOMAIN,
      details,
      toBeValidated,
      HashSet.of(ValidationError.Claim.CAN_BE_DISPLAYED)
    );
  }
}
