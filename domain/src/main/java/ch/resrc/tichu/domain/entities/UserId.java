package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.IdSequence;
import ch.resrc.tichu.domain.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.domain.value_objects.*;

import java.util.UUID;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.context;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.invariantViolated;

public class UserId extends DomainPrimitive<UserId, String> implements StringValueObject {

    private final UUID value;

    private UserId(String literal) {
        value = UUID.fromString(literal);
    }

    public static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notBlank(),
                        isUuid()
                ),
                context(UserId.class)
        );
    }

    public static Result<UserId, ValidationError> resultOf(String literal) {

        return validation().applyTo(literal).map(UserId::new);
    }

    public static UserId of(String literal) throws DomainProblemDetected {

        return UserId.resultOf(literal).getOrThrow(invariantViolated());
    }

    public static UserId random() {
        return UserId.of(UUID.randomUUID().toString());
    }

    @Override
    protected String getPrimitiveValue() {

        return value.toString();
    }

    public String asLiteral() {
        return getPrimitiveValue();
    }

    public interface Sequence extends IdSequence<UserId> {

        UserId.Sequence RANDOM = () -> UserId.of(UUID.randomUUID().toString());

        UserId nextId();
    }
}
