package ch.resrc.tichu.domain.value_objects;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.cannotBeUndefinedMsg;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.context;
import static ch.resrc.tichu.capabilities.validation.Validations.isUuid;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.capabilities.validation.Validations;
import ch.resrc.tichu.domain.DomainPrimitive;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;

public class Id extends DomainPrimitive<Id, String> implements StringValueObject {

  private final UUID value;

  private Id(String literal) {
    value = UUID.fromString(literal);
  }

  private Id(UUID vale) {
    this.value = vale;
  }

  @Override
  protected String getPrimitiveValue() {
    return value.toString();
  }

  public static Result<Id, ValidationError> resultOf(String literal) {
    return validation().applyTo(literal).map(Id::new);
  }

  @JsonCreator
  public static Id of(String literal) {
    return new Id(literal);
  }

  public static Validation<String, ValidationError> validation() {
    return Validations.chained(
      notNull(cannotBeUndefinedMsg()),
      isUuid()
    ).mapErrors(context(Id.class));
  }

  public static Id next() {
    return new Id(UUID.randomUUID());
  }

  public String asLiteral() {
    return getPrimitiveValue();
  }
}
