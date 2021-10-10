package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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

    // when:
    var errorOrTichus = Tichus.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrTichus::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTichus.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }
}
