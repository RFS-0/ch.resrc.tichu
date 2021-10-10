package ch.resrc.tichu.test.capabilities.habits.assertions;

import ch.resrc.tichu.capabilities.validation.*;
import org.hamcrest.*;

import static ch.resrc.tichu.capabilities.validation.ValidationError.*;
import static ch.resrc.tichu.test.capabilities.habits.assertions.CustomMatchers.*;
import static org.hamcrest.core.Is.*;

public class IsValidationError {

    public static Matcher<ValidationError> whereErrorMessage(Matcher<String> messageMatcher) {
        return whereAttribute(ValidationError::errorMessage, "error message", messageMatcher);
    }

    public static Matcher<ValidationError> hasNoOrigin() {
        return whereAttribute(ValidationError::origin, "origin", is(UNKNOWN_ORIGIN));
    }
}
