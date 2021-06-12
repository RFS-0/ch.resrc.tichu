package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
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
  void aLegalJoinCode_resultOf_validJoinCode(String legalJoinCode) {
    // when:
    var errorOrJoinCode = JoinCode.resultOf(legalJoinCode);

    // then:
    errorOrJoinCode.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrJoinCode::get);
    JoinCode joinCode = errorOrJoinCode.get();
    assertThat(joinCode.value()).isEqualTo(legalJoinCode);
  }

  @ParameterizedTest
  @EmptySource
  @NullSource
  void aBlankJoinCode_resultOf_expectedError(String blankJoinCode) {
    // given:
    ValidationError mustNotBeBlankError = DomainValidationErrors.errorDetails("value must not be blank")
      .apply(blankJoinCode);

    // when:
    var errorOrJoinCode = JoinCode.resultOf(blankJoinCode);

    // then:
    assertThatThrownBy(errorOrJoinCode::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrJoinCode.getLeft();
    assertThat(errors).contains(mustNotBeBlankError);
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
  void anIllegalUuid_resultOf_expectedError(String illegalUuid) {
    // given:
    ValidationError mustConsistOfEightAlphanumericCharacters = DomainValidationErrors.errorDetails("value must consist of eight alphanumeric characters")
      .apply(illegalUuid);

    // when:
    var errorOrId = JoinCode.resultOf(illegalUuid);

    // then:
    assertThatThrownBy(errorOrId::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrId.getLeft();
    assertThat(errors).contains(mustConsistOfEightAlphanumericCharacters);
  }
}
