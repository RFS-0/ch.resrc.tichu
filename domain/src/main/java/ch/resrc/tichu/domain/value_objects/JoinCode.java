package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class JoinCode extends DomainPrimitive<JoinCode, String> implements StringValueObject, Comparable<JoinCode> {

    public static final String JOIN_CODE_PATTERN = "^[a-zA-Z0-9]{8}$";

    private final String value;

    private JoinCode(String literal) {
        value = literal;
    }

    private static Validation<String, ValidationError> validation() {
        return chained(
                notBlank(),
                matches(JOIN_CODE_PATTERN, msg("value must consist of eight alphanumeric characters"))
        );
    }

    public static Result<JoinCode, ValidationError> resultOf(String literal) {
        return validation().applyTo(literal).map(JoinCode::new);
    }

    public static JoinCode next() {
        return new JoinCode(UUID.randomUUID().toString().split("-")[0]);
    }

    public static JoinCode of(String literal) {
        return resultOf(literal).getOrThrow(invariantViolated());
    }

    public String value() {
        return value;
    }

    @Override
    protected String getPrimitiveValue() {
        return value;
    }

    @Override
    public int compareTo(JoinCode other) {
        return this.value.compareTo(other.value);
    }
}
