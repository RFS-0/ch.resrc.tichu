package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EmailTest {

  @ParameterizedTest
  @ValueSource(strings = {"given.family@first.com", "valid@second.io"})
  void aLegalEmailAddress_resultOf_validCardPoints(String aLegalEmailAddress) {
    // when:
    var errorOrEmail = Email.resultOf(aLegalEmailAddress);

    // then:
    errorOrEmail.peekLeft(System.out::println);
    Email email = errorOrEmail.get();
    assertThat(email.value()).isEqualTo(aLegalEmailAddress);
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  void null_resultOf_validCardPoints(String input) {
    // given:
    ValidationError mustNotBeBlankError = DomainValidationErrors.errorDetails("value must not be blank").apply(input);

    // when:
    var errorOrEmail = Email.resultOf(input);

    // then:
    assertThatThrownBy(errorOrEmail::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrEmail.getLeft();
    assertThat(errors).contains(mustNotBeBlankError);
  }

  @ParameterizedTest
  @ValueSource(strings = {"!nvalid@valid.com", "valid@invalid"})
  void aIllegalEmailAddress_resultOf_expectedError(String aLegalEmailAddress) {
    // given:
    ValidationError invalidEmailAddressError = DomainValidationErrors.errorDetails("value must be a valid email address")
      .apply(aLegalEmailAddress);

    // when:
    var errorOrEmail = Email.resultOf(aLegalEmailAddress);

    // then:
    assertThatThrownBy(errorOrEmail::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrEmail.getLeft();
    assertThat(errors).contains(invalidEmailAddressError);
  }
}
