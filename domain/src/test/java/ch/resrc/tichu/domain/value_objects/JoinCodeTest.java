package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.hamcrest.Matchers.*;

class JoinCodeTest {

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
    void aValidJoinCode_resultOf_success(String validJoinCode) {
        // given, when:
        var result = JoinCode.resultOf(validJoinCode);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().getPrimitiveValue()).isEqualTo(validJoinCode);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void aBlankJoinCode_resultOf_failure(String blankJoinCode) {
        // given, when:
        var result = JoinCode.resultOf(blankJoinCode);

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
        var result = JoinCode.resultOf(invalidUuid);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("value must consist of eight alphanumeric characters")))
        );
    }
}
