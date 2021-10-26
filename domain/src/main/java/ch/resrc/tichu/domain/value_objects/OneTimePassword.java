package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class OneTimePassword extends DomainPrimitive<OneTimePassword, String> implements StringValueObject {

    public static final String OTP_PATTERN = "^[a-zA-Z0-9]{8}$";

    private final String value;

    private OneTimePassword(String literal) {
        value = literal;
    }

    private static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notBlank(),
                        matches(OTP_PATTERN, msg("value must consist of eight alphanumeric characters"))),
                context(OneTimePassword.class)
        );
    }

    public static Result<OneTimePassword, ValidationError> resultOf(String literal) {
        return validation().applyTo(literal).map(OneTimePassword::new);
    }

    public static OneTimePassword next() {
        return new OneTimePassword(UUID.randomUUID().toString().split("-")[0]);
    }

    public static OneTimePassword of(String literal) {
        return resultOf(literal).getOrThrow(invariantViolated());
    }

    public String value() {
        return value;
    }

    @Override
    protected String getPrimitiveValue() {
        return value;
    }
}
