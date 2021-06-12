package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;

import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.isUuid;
import static ch.resrc.tichu.capabilities.validation.Validations.notBlank;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustBeUuid;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeBlank;

public class Id {

  private final UUID value;

  private Id(String literal) {
    value = UUID.fromString(literal);
  }

  private Id(UUID vale) {
    this.value = vale;
  }

  private static Validation<Seq<ValidationError>, String> validation() {
    return chained(
      notBlank(mustNotBeBlank()),
      isUuid(mustBeUuid())
    );
  }

  public static Either<Seq<ValidationError>, Id> resultOf(String literal) {
    return validation().apply(literal).map(Id::new);
  }

  public static Id next() {
    return new Id(UUID.randomUUID());
  }

  public UUID value() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Id id = (Id) o;

    return value.equals(id.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
