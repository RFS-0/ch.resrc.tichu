package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TichusTest {

  @Test
  void legalValues_resultOf_validCardPoints() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    HashMap<Id, Tichu> validValues = HashMap.of(
      player1, Tichu.NONE,
      player2, Tichu.NONE,
      player3, Tichu.NONE,
      player4, Tichu.TICHU_CALLED
    );

    // when:
    var errorOrTichus = Tichus.resultOf(validValues);

    // then:
    errorOrTichus.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrTichus::get);
    Tichus tichus = errorOrTichus.get();
    assertThat(tichus.values()).isEqualTo(validValues);
  }

  @Test
  void legalValuesForFivePlayers_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    Id player5 = Id.next();
    HashMap<Id, Tichu> validValues = HashMap.of(
      player1, Tichu.NONE,
      player2, Tichu.NONE,
      player3, Tichu.NONE,
      player4, Tichu.NONE,
      player5, Tichu.TICHU_CALLED
    );
    ValidationError mustNotBeDefinedMoreThanOncePerPlayerError = DomainValidationErrors.errorDetails("must not be defined more than once per player")
      .apply(validValues.length());

    // when:
    var errorOrTichus = Tichus.resultOf(validValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeDefinedMoreThanOncePerPlayerError);
  }

  @ParameterizedTest
  @NullSource
  void null_resultOf_expectedError(Map<Id, Tichu> input) {
    // given:
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(null);

    // when:
    var errorOrTichus = Tichus.resultOf(input);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullValue_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    Tichu nullValue = null;
    HashMap<Id, Tichu> invalidValues = HashMap.of(
      player1, Tichu.NONE,
      player2, Tichu.NONE,
      player3, Tichu.NONE,
      player4, nullValue
    );
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(invalidValues);

    // when:
    var errorOrTichus = Tichus.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullKey_resultOf_expectedError() {
    // given:
    Id nullKey = null;
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    Tichu nullValue = null;
    HashMap<Id, Tichu> invalidValues = HashMap.of(
      nullKey, Tichu.NONE,
      player2, Tichu.NONE,
      player3, Tichu.NONE,
      player4, nullValue
    );
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(invalidValues);
    ;

    // when:
    var errorOrTichus = Tichus.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }
}
