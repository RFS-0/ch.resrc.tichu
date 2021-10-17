package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.*;
import ch.resrc.tichu.domain.errorhandling.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

/**
 * Uniquely identifies a namespace. Each domain entity is associated to a namespace.
 * A client can only access the entities of a namespace if she is authorized to access
 * the namespace.
 */
public final class NamespaceId extends DomainPrimitive<NamespaceId, String> implements StringValueObject {

    private final UUID value;

    private NamespaceId(String literal) {
        value = UUID.fromString(literal);
    }

    public static Validation<String, ValidationError> validation() {

        return Validations.chained(
                notNull(),
                isUuid()
        ).mapErrors(context(NamespaceId.class));
    }

    public static Result<NamespaceId, ValidationError> resultOf(String literal) {

        return validation().applyTo(literal).map(NamespaceId::new);
    }

    public static NamespaceId of(String literal) throws DomainProblemDetected {

        return NamespaceId.resultOf(literal).getOrThrow(invariantViolated());
    }

    @Override
    protected String getPrimitiveValue() {

        return value.toString();
    }

    public interface Sequence extends IdSequence<NamespaceId> {

        Sequence RANDOM = () -> NamespaceId.of(UUID.randomUUID().toString());

        NamespaceId nextId();
    }

    public static NamespaceId publicNamespace() {
        return NamespaceId.of("51bfc8c1-50f9-4c2a-bcce-30c787c0d9f6");
    }
}
