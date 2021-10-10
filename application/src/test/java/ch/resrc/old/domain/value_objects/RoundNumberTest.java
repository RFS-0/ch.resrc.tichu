package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RoundNumberTest {

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100})
  void legalValues_resultOf_validRoundNumbers(int legalRoundNumber) {
    // when:
    var errorOrRoundNumber = RoundNumber.resultOf(legalRoundNumber);

    // then:
    errorOrRoundNumber.peekLeft(System.out::println);
    RoundNumber roundNumber = errorOrRoundNumber.get();
    assertThat(roundNumber.value()).isEqualTo(legalRoundNumber);
  }

  @Test
  void tooSmallValue_resultOf_expectedError() {
    // given:
    int tooSmallValue = 0;
    ValidationError mustNotBeSmallerThanOneError = DomainValidationErrors.errorDetails("must not be smaller than one")
      .apply(tooSmallValue);


    // when:
    var errorOrRoundNumber = RoundNumber.resultOf(tooSmallValue);

    // then:
    assertThatThrownBy(errorOrRoundNumber::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRoundNumber.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeSmallerThanOneError);
  }
}
