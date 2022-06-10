package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import com.fasterxml.jackson.annotation.*;

import java.util.regex.*;

import static ch.resrc.tichu.capabilities.validation.ValidationError.Claim.*;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class Email extends DomainPrimitive<Email, String> implements StringValueObject, Comparable<Email> {

    private static final String EMAIL_ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private final String value;

    private Email(String literal) {
        value = literal;
    }

    private static Validation<String, ValidationError> validation() {
        return modified(
                chained(
                        notBlank(),
                        matches(EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE, msg("value must be a valid email address"))
                ),
                context(Email.class),
                claimed(UNINTERESTING_VALUE)
        );
    }

    public static Result<Email, ValidationError> resultOf(String literal) {
        return validation().applyTo(literal).map(Email::new);
    }

    @JsonCreator
    public static Email of(String literal) {
        return resultOf(literal).getOrThrow(invariantViolated());
    }

    public String asLiteral() {
        return value;
    }

    @Override
    protected String getPrimitiveValue() {
        return value;
    }

    @Override
    public int compareTo(Email other) {
        return this.value.compareTo(other.value);
    }
}
