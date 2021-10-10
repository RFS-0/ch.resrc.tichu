package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class RoundNumber extends DomainPrimitive<RoundNumber, Integer> implements StringValueObject, Comparable<RoundNumber> {

    private final int value;

    private RoundNumber(int value) {
        this.value = value;
    }

    private static Validation<Integer, ValidationError> validation() {
        return min(1);
    }

    public static Result<RoundNumber, ValidationError> resultOf(int value) {
        return validation().applyTo(value).map(RoundNumber::new);
    }

    public RoundNumber increment() {
        return new RoundNumber(value + 1);
    }

    @Override
    protected Integer getPrimitiveValue() {
        return value;
    }

    @Override
    public int compareTo(RoundNumber other) {
        return Integer.compare(this.value, other.value);
    }
}
