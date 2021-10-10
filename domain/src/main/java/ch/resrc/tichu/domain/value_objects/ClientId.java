package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.errorhandling.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

/**
 * Uniquely identifies a client who invokes a use case
 */
public final class ClientId extends DomainPrimitive<ClientId, String> implements StringValueObject {

    private final UUID value;

    private ClientId(String literal) {
        value = UUID.fromString(literal);
    }

    public static Validation<String, ValidationError> validation() {

        return Validations.chained(
                notNull(),
                isUuid()
        ).mapErrors(context(ClientId.class));
    }

    public static Result<ClientId, ValidationError> resultOf(String literal) {

        return validation().applyTo(literal).map(ClientId::new);
    }

    public static ClientId of(String literal) throws DomainProblemDetected {

        return ClientId.resultOf(literal).getOrThrow(invariantViolated());
    }

    @Override
    protected String getPrimitiveValue() {

        return value.toString();
    }

    public interface Sequence extends IdSequence<ClientId> {

        Sequence RANDOM = () -> ClientId.of(UUID.randomUUID().toString());

        ClientId nextId();
    }
}
