package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

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
