package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TichusTest {

  @Test
  @DisplayName("The [resultOf] legal tichu values [NONE, NONE, NONE, TICHU_CALLED] for four players are valid tichus")
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
  @DisplayName("The [resultOf] legal tichu values [NONE, NONE, NONE, NONE, TICHU_CALLED] for five players is the validation error [MUST_NOT_BE_DEFINED_MORE_THAN_ONCE_PER_PLAYER]")
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
    ValidationError mustNotBeDefinedMoreThanOncePerPlayerError = TichusValidationErrors.MUST_NOT_BE_DEFINED_MORE_THAN_ONCE_PER_PLAYER.get();

    // when:
    var errorOrTichus = Tichus.resultOf(validValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeDefinedMoreThanOncePerPlayerError);
  }

  @ParameterizedTest(name = "The [resultOf] illegal input [{arguments}] is the validation error [MUST_NOT_BE_NULL]")
  @NullSource
  void null_resultOf_expectedError(Map<Id, Tichu> input) {
    // given:
    ValidationError mustNotBeNullError = TichusValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrTichus = Tichus.resultOf(input);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] an illegal tichu value [null] for four players is the validation error [MUST_NOT_BE_NULL]")
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
    ValidationError mustNotBeNullError = TichusValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrTichus = Tichus.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] legal tichu values [NONE, NONE, NONE, NONE] an one illegal player id [null] is the validation error [MUST_NOT_BE_NULL]")
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
    ValidationError mustNotBeNullError = TichusValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrTichus = Tichus.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }
}
