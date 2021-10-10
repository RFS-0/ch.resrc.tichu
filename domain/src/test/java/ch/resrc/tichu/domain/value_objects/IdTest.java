package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class IdTest {

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "d0dbf509-c942-40bd-a6a1-d01b912d2615",
                    "bf60d572-2cda-4bb7-8ff1-d6e383cf63b7",
                    "51e0b5e3-1127-4e97-9c62-4c11d4b12373",
                    "6497a603-0077-4a9b-a999-fff39901d1da",
                    "06f6929e-e217-4862-9a04-765f70c9e14d"
            }
    )
    void aValidUuid_resultOf_success(String validUuid) {
        // given, when:
        var result = Id.resultOf(validUuid);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().getPrimitiveValue().toString()).isEqualTo(validUuid);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void aBlankUuid_resultOf_failure(String blankUuid) {
        // given, when:
        var result = Id.resultOf(blankUuid);

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
                    "d0dbf509-c942-40bd-a6a1-d01b912d261!",
                    "bf60d572-2cda-4bb7",
                    "51e0b5e3-1127-4e97-9c62-4c11d4b123733",
                    "6497a603-0077-4a9b-a999",
                    "   06f6929e-e217-4862-9a04-765f70c9e14d"
            }
    )
    void anInvalidUuid_resultOf_failure(String invalidUuid) {
        // given, when:
        var result = Id.resultOf(invalidUuid);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must be a UUID")))
        );
    }
}