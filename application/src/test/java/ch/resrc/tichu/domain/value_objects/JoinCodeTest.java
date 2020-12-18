package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class JoinCodeTest {

  @ParameterizedTest(name = "The [resultOf] the {index}. legal join code [{arguments}] is a valid join code")
  @ValueSource(
    strings = {
      "d0dbf509",
      "bf60d572",
      "51E0b5e3",
      "12345678",
      "ABCDEFGH"
    }
  )
  void aLegalJoinCode_resultOf_validJoinCode(String legalJoinCode) {
    // when:
    var errorOrJoinCode = JoinCode.resultOf(legalJoinCode);

    // then:
    errorOrJoinCode.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrJoinCode::get);
    JoinCode joinCode = errorOrJoinCode.get();
    assertThat(joinCode.value()).isEqualTo(legalJoinCode);
  }

  @ParameterizedTest(name = "The [resultOf] the {index}. blank join code [{arguments}] is the validation error [MUST_NOT_BE_BLANK]")
  @EmptySource
  @NullSource
  void aBlankJoinCode_resultOf_expectedError(String blankJoinCode) {
    // given:
    ValidationError mustNotBeBlankError = JoinCodeValidationErrors.MUST_NOT_BE_BLANK.get();

    // when:
    var errorOrJoinCode = JoinCode.resultOf(blankJoinCode);

    // then:
    assertThatThrownBy(errorOrJoinCode::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrJoinCode.getLeft();
    assertThat(errors).contains(mustNotBeBlankError);
  }

  @ParameterizedTest(name = "The [resultOf] the {index}. illegal join code [{arguments}] is the validation error [MUST_CONSIST_OF_EIGHT_ALPHANUMERIC_CHARACTERS]")
  @ValueSource(
    strings = {
      "1234567",
      "ABCDEFG",
      "1234567!",
      "ABCDEFG ",
      ".bcdefg!"
    }
  )
  void anIllegalUuid_resultOf_expectedError(String illegalUuid) {
    // given:
    ValidationError mustConsistOfEightAlphanumericCharacters = JoinCodeValidationErrors.MUST_CONSIST_OF_EIGHT_ALPHANUMERIC_CHARACTERS.get();

    // when:
    var errorOrId = JoinCode.resultOf(illegalUuid);

    // then:
    assertThatThrownBy(errorOrId::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrId.getLeft();
    assertThat(errors).contains(mustConsistOfEightAlphanumericCharacters);
  }
}
