package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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
