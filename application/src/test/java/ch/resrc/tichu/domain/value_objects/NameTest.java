package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
  void aLegalName_resultOf_validName(String legalName) {
    // when:
    var errorOrName = Name.resultOf(legalName);

    // then:
    errorOrName.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrName::get);
    Name name = errorOrName.get();
    assertThat(name.value()).isEqualTo(legalName);
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
  void anIllegalName_resultOf_expectedError(String blankName) {
    // given
    var mustNotBeBlankError = ValidationError.of(
      "value must not be blank"
    );

    // when:
    var errorOrName = Name.resultOf(blankName);

    // then:
    assertThatThrownBy(errorOrName::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrName.getLeft();
    //assertThat(errors).hasSize(1);
    //assertThat(errors).contains(mustNotBeBlankError);
  }
}
