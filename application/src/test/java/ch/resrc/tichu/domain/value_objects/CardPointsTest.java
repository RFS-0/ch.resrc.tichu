package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CardPointsTest {

  @Test
  @DisplayName("The [resultOf] legal values [-25, 125] are valid card points")
  void legalValues_resultOf_validCardPoints() {
    // given:
    final var aLegalId = Id.next();
    final var anotherLegalId = Id.next();
    HashMap<String, String> inputValues = HashMap.of(
      aLegalId.value().toString(), "-25",
      anotherLegalId.value().toString(), "125"
    );
    HashMap<Id, Integer> expectedValues = HashMap.of(
      aLegalId, -25,
      anotherLegalId, 125
    );

    // when:
    var errorOrCardPoints = CardPoints.resultOf(inputValues);

    // then:
    errorOrCardPoints.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrCardPoints::get);
    CardPoints cardPoints = errorOrCardPoints.get();
    assertThat(cardPoints.values()).isEqualTo(expectedValues);
  }

  @ParameterizedTest(name = "The [resultOf] illegal value [{0}] is the validation error [MUST_NOT_BE_NULL]")
  @NullSource
  void null_resultOf_expectedError(Map<String, String> input) {
    // given:
    ValidationError mustNotBeNullError = CardPointsValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(input);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] only one team having card points is the validation error [MUST_BE_DEFINED_FOR_BOTH_TEAMS]")
  void onlyOneTeamHasCardPoints_resultOf_expectedError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var aLegalValue = "100";
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, aLegalValue
    );
    ValidationError mustBeDefinedForBothTeamsError = CardPointsValidationErrors.MUST_BE_DEFINED_FOR_BOTH_TEAMS.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustBeDefinedForBothTeamsError);
  }

  @Test
  @DisplayName("The [resultOf] values not divisible by 5 [-24, 124] is the validation error [MUST_BE_DIVISIBLE_BY_5]")
  void valuesNotDivisibleBy5_resultOf_expectedError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var anotherLegalId = Id.next().value().toString();
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, "-24",
      anotherLegalId, "124"
    );
    ValidationError mustBeDivisibleBy5Error = CardPointsValidationErrors.MUST_BE_DIVISIBLE_BY_5.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    assertThat(errorOrCardPoints.getLeft()).contains(mustBeDivisibleBy5Error);
  }

  @Test
  @DisplayName("The [resultOf] a too small value [-30] is the validation error [MUST_BE_HIGHER_THAN_MINUS_25]")
  void tooSmallValue_resultOf_expectedValidationError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var anotherLegalId = Id.next().value().toString();
    int tooSmallValue = -30;
    int legalValue = 105;
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, String.valueOf(tooSmallValue),
      anotherLegalId, String.valueOf(legalValue)
    );
    ValidationError mustBeHigherThanMinus25Error = CardPointsValidationErrors.MUST_NOT_BE_SMALLER_THAN_MINUS_25.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).contains(mustBeHigherThanMinus25Error);
  }

  @Test
  @DisplayName("The [resultOf] a too large value [130] is the validation error [MUST_BE_LOWER_THAN_125]")
  void tooLargeValue_resultOf_expectedValidationError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var anotherLegalId = Id.next().value().toString();
    int tooLargeValue = 130;
    int legalValue = 100;
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, String.valueOf(tooLargeValue),
      anotherLegalId, String.valueOf(legalValue)
    );
    ValidationError mustBeLowerThan125Error = CardPointsValidationErrors.MUST_NOT_BE_HIGHER_THAN_125.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).contains(mustBeLowerThan125Error);
  }

  @Test
  @DisplayName("The [resultOf] legal values  [50, 55] whose total is not equal to 100 is the validation error [MUST_BE_EQUAL_TO_100]")
  void totalNotEqualTo100_resultOf_expectedValidationError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var anotherLegalId = Id.next().value().toString();
    String aLegalValue = "50";
    String anotherLegalValue = "55";
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, aLegalValue,
      anotherLegalId, anotherLegalValue
    );
    ValidationError mustBeEqualTo100Error = CardPointsValidationErrors.MUST_BE_EQUAL_TO_100.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).contains(mustBeEqualTo100Error);
  }

  @Test
  @DisplayName("The [resultOf] a too large value not divisible by 5 [141] and a too small value [-30] are the validation errors [MUST_BE_DIVISIBLE_BY_5, MUST_BE_HIGHER_THAN_MINUS_25, MUST_BE_LOWER_THAN_125, MUST_BE_EQUAL_TO_100]")
  void tooLargeValueNotDivisibleBy5_tooSmallValue_resultOf_expectedValidationError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var anotherLegalId = Id.next().value().toString();
    final var tooLargeValue = "141";
    final var tooSmallValue = "-30";
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, tooLargeValue,
      anotherLegalId, tooSmallValue
    );
    ValidationError mustBeDivisibleBy5Error = CardPointsValidationErrors.MUST_BE_DIVISIBLE_BY_5.get();
    ValidationError mustBeHigherThanMinus25Error = CardPointsValidationErrors.MUST_NOT_BE_SMALLER_THAN_MINUS_25.get();
    ValidationError mustBeLowerThan125Error = CardPointsValidationErrors.MUST_NOT_BE_HIGHER_THAN_125.get();
    ValidationError mustBeEqualTo100Error = CardPointsValidationErrors.MUST_BE_EQUAL_TO_100.get();

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).hasSize(4);
    assertThat(errors).containsAll(
      List.of(
        mustBeDivisibleBy5Error,
        mustBeHigherThanMinus25Error,
        mustBeLowerThan125Error,
        mustBeEqualTo100Error
      )
    );
  }
}
