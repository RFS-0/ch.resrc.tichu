package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
  void aLegalUuid_resultOf_validId(String legalUuid) {
    // when:
    var errorOrId = Id.resultOf(legalUuid);

    // then:
    errorOrId.peekLeft(System.out::println);
    Id id = errorOrId.get();
    assertThat(id.value().toString()).isEqualTo(legalUuid);
  }

  @ParameterizedTest
  @EmptySource
  @NullSource
  void aBlankUuid_resultOf_expectedError(String blankUuid) {
    // given:
    ValidationError mustNotBeBlankError = DomainValidationErrors.mustNotBeBlank().apply(blankUuid);

    // when:
    var errorOrId = Id.resultOf(blankUuid);

    // then:
    assertThatThrownBy(errorOrId::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrId.getLeft();
    assertThat(errors).contains(mustNotBeBlankError);
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
  void anIllegalUuid_resultOf_expectedError(String illegalUuid) {
    // given:
    ValidationError mustBeUuidError = DomainValidationErrors.mustBeUuid().apply(illegalUuid);

    // when:
    var errorOrId = Id.resultOf(illegalUuid);

    // then:
    assertThatThrownBy(errorOrId::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrId.getLeft();
    assertThat(errors).contains(mustBeUuidError);
  }
}
