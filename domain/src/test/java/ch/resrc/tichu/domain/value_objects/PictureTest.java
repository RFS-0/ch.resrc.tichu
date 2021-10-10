package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.hamcrest.Matchers.*;

class PictureTest {

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "http://www.test.com",
                    "https://www.test.ch",
                    "ftp://example.com/home/joe/employee.csv",
                    "https://www.test.io?pictureName=test",
                    "http://www.test.io?pictureName=test",
                    "https://www.test.io?pictureName=test",
            }
    )
    void aValidPicture_resultOf_success(String validPicture) {
        // given, when:
        var result = Picture.resultOf(validPicture);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().getPrimitiveValue()).isEqualTo(validPicture);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void aBlankPicture_resultOf_failure(String blankPicture) {
        // given, when:
        var result = Picture.resultOf(blankPicture);

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
                    "te;st.com",
                    "192.168.1.1:8080",
                    "www.test.ch/!!-to-picture",
                    "www.test.io?  =test",
                    "kttp://www.test.io?pictureName=test",
                    "attps://www.test.io?pictureName=test",
            }
    )
    void anInvalidUuid_resultOf_failure(String invalidPicture) {
        // given, when:
        var result = Picture.resultOf(invalidPicture);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must be an URL")))
        );
    }
}
