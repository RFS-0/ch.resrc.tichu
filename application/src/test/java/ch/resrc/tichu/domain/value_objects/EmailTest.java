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

class EmailTest {

  @ParameterizedTest
  @ValueSource(strings = {"given.family@first.com", "valid@second.io"})
  void aLegalEmailAddress_resultOf_validCardPoints(String aLegalEmailAddress) {
    // when:
    var errorOrEmail = Email.resultOf(aLegalEmailAddress);

    // then:
    errorOrEmail.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrEmail::get);
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
