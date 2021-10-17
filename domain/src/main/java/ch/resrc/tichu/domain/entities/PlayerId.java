package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.*;
import ch.resrc.tichu.domain.errorhandling.*;
import ch.resrc.tichu.domain.value_objects.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class PlayerId extends DomainPrimitive<PlayerId, String> implements StringValueObject {

    private final UUID value;

    private PlayerId(String literal) {
        value = UUID.fromString(literal);
    }

    public static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notNull(),
                        isUuid()
                ),
                context(PlayerId.class)
        );
    }

    public static Result<PlayerId, ValidationError> resultOf(String literal) {

        return validation().applyTo(literal).map(PlayerId::new);
    }

    public static PlayerId of(String literal) throws DomainProblemDetected {

        return PlayerId.resultOf(literal).getOrThrow(invariantViolated());
    }

    @Override
    protected String getPrimitiveValue() {

        return value.toString();
    }

    public interface Sequence extends IdSequence<PlayerId> {

        PlayerId.Sequence RANDOM = () -> PlayerId.of(UUID.randomUUID().toString());

        PlayerId nextId();
    }
}
