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

public class GameId extends DomainPrimitive<GameId, String> implements StringValueObject {

    private final UUID value;

    private GameId(String literal) {
        value = UUID.fromString(literal);
    }

    public static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notNull(),
                        isUuid()
                ),
                context(GameId.class)
        );
    }

    public static Result<GameId, ValidationError> resultOf(String literal) {

        return validation().applyTo(literal).map(GameId::new);
    }

    public static GameId of(String literal) throws DomainProblemDetected {

        return GameId.resultOf(literal).getOrThrow(invariantViolated());
    }

    @Override
    protected String getPrimitiveValue() {

        return value.toString();
    }

    public interface Sequence extends IdSequence<GameId> {

        GameId.Sequence RANDOM = () -> GameId.of(UUID.randomUUID().toString());

        GameId nextId();
    }
}