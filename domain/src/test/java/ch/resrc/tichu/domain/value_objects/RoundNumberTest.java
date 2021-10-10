package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.hamcrest.Matchers.*;

class RoundNumberTest {

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100})
  void validValues_resultOf_success(int validRoundNumber) {
    // given, when:
    var result = RoundNumber.resultOf(validRoundNumber);

    // then:
    result.failureEffect(System.out::println);
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.value().getPrimitiveValue()).isEqualTo(validRoundNumber);
  }

  @Test
  void tooSmallValue_resultOf_failure() {
    // given:
    int tooSmallValue = 0;

    // when:
    var result = RoundNumber.resultOf(tooSmallValue);

    // then:
    assertThat(result.isFailure()).isTrue();
    AssertionHabits.assertThat(
            result.errors(),
            contains(whereErrorMessage(containsString("must greater or equal to <1>")))
    );
  }
}
