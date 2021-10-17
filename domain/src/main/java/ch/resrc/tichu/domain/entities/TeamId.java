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

public class TeamId extends DomainPrimitive<TeamId, String> implements StringValueObject {

    private final UUID value;

    private TeamId(String literal) {
        value = UUID.fromString(literal);
    }

    public static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notNull(),
                        isUuid()
                ),
                context(TeamId.class)
        );
    }

    public static Result<TeamId, ValidationError> resultOf(String literal) {

        return validation().applyTo(literal).map(TeamId::new);
    }

    public static TeamId of(String literal) throws DomainProblemDetected {

        return TeamId.resultOf(literal).getOrThrow(invariantViolated());
    }

    @Override
    protected String getPrimitiveValue() {

        return value.toString();
    }

    public interface Sequence extends IdSequence<TeamId> {

        TeamId.Sequence RANDOM = () -> TeamId.of(UUID.randomUUID().toString());

        TeamId nextId();
    }
}
