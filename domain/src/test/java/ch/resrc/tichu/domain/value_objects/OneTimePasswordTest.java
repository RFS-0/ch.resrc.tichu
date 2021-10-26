package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class OneTimePasswordTest {

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "d0dbf509",
                    "bf60d572",
                    "51E0b5e3",
                    "12345678",
                    "ABCDEFGH"
            }
    )
    void aValidOneTimePassword_resultOf_success(String validOneTimePassword) {
        // given, when:
        var result = OneTimePassword.resultOf(validOneTimePassword);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().getPrimitiveValue()).isEqualTo(validOneTimePassword);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void aBlankOneTimePassword_resultOf_failure(String blankOneTimePassword) {
        // given, when:
        var result = OneTimePassword.resultOf(blankOneTimePassword);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not be blank")))
        );
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "1234567",
                    "ABCDEFG",
                    "1234567!",
                    "ABCDEFG ",
                    ".bcdefg!"
            }
    )
    void anInvalidUuid_resultOf_failure(String invalidUuid) {
        // given, when:
        var result = OneTimePassword.resultOf(invalidUuid);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("value must consist of eight alphanumeric characters")))
        );
    }
}