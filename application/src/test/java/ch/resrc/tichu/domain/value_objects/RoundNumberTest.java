package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RoundNumberTest {

  @ParameterizedTest(name = "The [resultOf] a legal round number [{arguments}] is a valid round number")
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100})
  void legalValues_resultOf_validRoundNumbers(int legalRoundNumber) {
    // when:
    var errorOrRoundNumber = RoundNumber.resultOf(legalRoundNumber);

    // then:
    errorOrRoundNumber.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrRoundNumber::get);
    RoundNumber roundNumber = errorOrRoundNumber.get();
    assertThat(roundNumber.value()).isEqualTo(legalRoundNumber);
  }

  @Test
  @DisplayName("The [resultOf] a too small round number [0] is the validation error [MUST_NOT_BE_SMALLER_THAN_ONE]")
  void tooSmallValue_resultOf_expectedError() {
    // given:
    int tooSmallValue = 0;
    ValidationError mustNotBeSmallerThanOneError = RoundNumberValidationErrors.MUST_NOT_BE_SMALLER_THAN_ONE.get();


    // when:
    var errorOrRoundNumber = RoundNumber.resultOf(tooSmallValue);

    // then:
    assertThatThrownBy(errorOrRoundNumber::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRoundNumber.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeSmallerThanOneError);
  }
}
