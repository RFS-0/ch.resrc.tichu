package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.hamcrest.Matchers.*;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"given.family@first.com", "valid@second.io"})
    void aValidEmailAddress_resultOf_success(String aValidEmailAddress) {
        // given, when:
        var result = Email.resultOf(aValidEmailAddress);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().getPrimitiveValue()).isEqualTo(aValidEmailAddress);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void null_resultOf_failure(String invalidInput) {
        // given, when:
        var result = Email.resultOf(invalidInput);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(
                        whereErrorMessage(containsString("must not be blank"))
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"!nvalid@valid.com", "valid@invalid"})
    void anInvalidEmailAddress_resultOf_failure(String invalidEmail) {
        // given, when:
        var result = Email.resultOf(invalidEmail);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(
                        whereErrorMessage(containsString("value must be a valid email address"))
                )
        );
    }
}