package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CardPointsTest {

  @Test
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

  @ParameterizedTest
  @NullSource
  void null_resultOf_expectedError(Map<String, String> input) {
    // given:
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(input);

    // when:
    var errorOrCardPoints = CardPoints.resultOf(input);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void onlyOneTeamHasCardPoints_resultOf_expectedError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var aLegalValue = "100";
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, aLegalValue
    );
    ValidationError mustBeDefinedForBothTeamsError = DomainValidationErrors.errorDetails("both teams must have card points")
      .apply(invalidValues.keySet().length());

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustBeDefinedForBothTeamsError);
  }

  @Test
  void valuesNotDivisibleBy5_resultOf_expectedError() {
    // given:
    final var aLegalId = Id.next().value().toString();
    final var anotherLegalId = Id.next().value().toString();
    HashMap<String, String> invalidValues = HashMap.of(
      aLegalId, "-24",
      anotherLegalId, "124"
    );
    ValidationError mustBeDivisibleBy5Error = DomainValidationErrors.errorDetails("the total of card points must be divisible by 5")
      .apply(List.of(124, -24).sorted());

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    assertThat(errorOrCardPoints.getLeft()).contains(mustBeDivisibleBy5Error);
  }

  @Test
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
    ValidationError mustBeHigherThanMinus25Error = DomainValidationErrors.errorDetails("the total of card points of a team can not be smaller than -25")
      .apply(List.of(tooSmallValue, legalValue).sorted());

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).contains(mustBeHigherThanMinus25Error);
  }

  @Test
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
    ValidationError mustBeLowerThan125Error = DomainValidationErrors.errorDetails("the total of card points of a team can not be larger than 125")
      .apply(List.of(tooLargeValue, legalValue).sorted());

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).contains(mustBeLowerThan125Error);
  }

  @Test
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
    ValidationError mustBeEqualTo100Error = DomainValidationErrors.errorDetails("the sum of card points must be equal to 100")
      .apply(List.of(50, 55).sum());

    // when:
    var errorOrCardPoints = CardPoints.resultOf(invalidValues);

    // then:
    assertThatThrownBy(errorOrCardPoints::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrCardPoints.getLeft();
    assertThat(errors).contains(mustBeEqualTo100Error);
  }

  @Test
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

    final var cardPointValues = List.of(-30, 141).toList().sorted();

    ValidationError mustBeDivisibleBy5Error = DomainValidationErrors.errorDetails("the total of card points must be divisible by 5")
      .apply(cardPointValues);
    ValidationError mustBeHigherThanMinus25Error = DomainValidationErrors.errorDetails("the total of card points of a team can not be smaller than -25")
      .apply(cardPointValues);
    ValidationError mustBeLowerThan125Error = DomainValidationErrors.errorDetails("the total of card points of a team can not be larger than 125")
      .apply(cardPointValues);
    ValidationError mustBeEqualTo100Error = DomainValidationErrors.errorDetails("the sum of card points must be equal to 100")
      .apply(cardPointValues.sum());

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
