package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.*;

import java.util.UUID;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.invariantViolated;

public class Otp extends DomainPrimitive<Otp, String> implements StringValueObject {

    public static final String OTP_PATTERN = "^[a-zA-Z0-9]{8}$";

    private final String value;

    private Otp(String literal) {
        value = literal;
    }

    private static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notBlank(),
                        matches(OTP_PATTERN, msg("value must consist of eight alphanumeric characters"))),
                context(Otp.class)
        );
    }

    public static Result<Otp, ValidationError> resultOf(String literal) {
        return validation().applyTo(literal).map(Otp::new);
    }

    public static Otp next() {
        return new Otp(UUID.randomUUID().toString().split("-")[0]);
    }

    public static Otp of(String literal) {
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
