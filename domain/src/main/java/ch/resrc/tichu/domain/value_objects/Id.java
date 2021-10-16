package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class Id extends DomainPrimitive<Id, UUID> implements StringValueObject, Comparable<Id> {

    private final UUID value;

    private Id(String literal) {
        value = UUID.fromString(literal);
    }

    private Id(UUID vale) {
        this.value = vale;
    }

    private static Validation<String, ValidationError> validation() {
        return chained(
                notBlank(),
                isUuid()
        );
    }

    public static Result<Id, ValidationError> resultOf(String literal) {
        return validation().applyTo(literal).map(Id::new);
    }

    public static Id next() {
        return new Id(UUID.randomUUID());
    }

    public static Id of(String literal) {
        return resultOf(literal).getOrThrow(invariantViolated());
    }

    @Override
    protected UUID getPrimitiveValue() {
        return value;
    }

    @Override
    public int compareTo(Id other) {
        return this.value.compareTo(other.value);
    }
}
