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

class PictureTest {

  @ParameterizedTest
  @ValueSource(
    strings = {
      "http://www.test.com",
      "https://www.test.ch",
      "ftp://example.com/home/joe/employee.csv",
      "https://www.test.io?pictureName=test",
      "http://www.test.io?pictureName=test",
      "https://www.test.io?pictureName=test",
    }
  )
  void aLegalPicture_resultOf_validJoinCode(String legalPicture) {
    // when:
    var errorOrPicture = Picture.resultOf(legalPicture);

    // then:
    errorOrPicture.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrPicture::get);
    Picture picture = errorOrPicture.get();
    assertThat(picture.value()).isEqualTo(legalPicture);
  }

  @ParameterizedTest
  @EmptySource
  @NullSource
  void aBlankPicture_resultOf_expectedError(String blankPicture) {
    // given:
    ValidationError mustNotBeBlankError = DomainValidationErrors.errorDetails("value must not be blank")
      .apply(blankPicture);

    // when:
    var errorOrPicture = Picture.resultOf(blankPicture);

    // then:
    assertThatThrownBy(errorOrPicture::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrPicture.getLeft();
    assertThat(errors).contains(mustNotBeBlankError);
  }

  @ParameterizedTest
  @ValueSource(
    strings = {
      "te;st.com",
      "192.168.1.1:8080",
      "www.test.ch/!!-to-picture",
      "www.test.io?  =test",
      "kttp://www.test.io?pictureName=test",
      "attps://www.test.io?pictureName=test",
    }
  )
  void anIllegalUuid_resultOf_expectedError(String illegalPicture) {
    // given:
    var mustBeUrlError = DomainValidationErrors.errorDetails("value must be a valid URL")
      .apply(illegalPicture);

    // when:
    var errorOrPicture = Picture.resultOf(illegalPicture);

    // then:
    assertThatThrownBy(errorOrPicture::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrPicture.getLeft();
    assertThat(errors).contains(mustBeUrlError);
  }
}
