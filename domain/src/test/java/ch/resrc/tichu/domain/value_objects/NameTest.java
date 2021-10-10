package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class NameTest {

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "a",
                    "ab",
                    "ABC",
                    "ABCD12",
                    "1234",
                    "1234",
                    "!@a[]'"
            }
    )
    void aValidName_resultOf_success(String validName) {
        // given, when:
        var result = Name.resultOf(validName);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().getPrimitiveValue()).isEqualTo(validName);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(
            strings = {
                    "",
                    "  ",
                    "\t",
                    "\t\t"
            }
    )
    void anInvalidName_resultOf_failure(String blankName) {
        // given, when:
        var result = Name.resultOf(blankName);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not be blank")))
        );
    }
}
