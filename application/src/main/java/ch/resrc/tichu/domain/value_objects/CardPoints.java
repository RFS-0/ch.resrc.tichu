package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allDivisibleBy;
import static ch.resrc.tichu.capabilities.validation.Validations.allMax;
import static ch.resrc.tichu.capabilities.validation.Validations.allMin;
import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.equalTo;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_BE_DEFINED_FOR_BOTH_TEAMS;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_BE_DIVISIBLE_BY_5;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_BE_EQUAL_TO_100;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_HAVE_VALID_IDS;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_HAVE_VALID_POINTS;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_NOT_BE_HIGHER_THAN_125;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_NOT_BE_NULL;
import static ch.resrc.tichu.domain.value_objects.CardPointsValidationErrors.MUST_NOT_BE_SMALLER_THAN_MINUS_25;

public class CardPoints {

  private final Map<Id, Integer> values;

  private CardPoints(Map<Id, Integer> values) {
    this.values = values;
  }

  private static Validation<Seq<ValidationError>, Map<Id, Integer>> validation() {
    return allOf(
      attribute(x -> x.keySet().length(), equalTo(2, MUST_BE_DEFINED_FOR_BOTH_TEAMS)),
      attribute(Map::values, allDivisibleBy(5, MUST_BE_DIVISIBLE_BY_5)),
      attribute(Map::values, allMin(-25, MUST_NOT_BE_SMALLER_THAN_MINUS_25)),
      attribute(Map::values, allMax(125, MUST_NOT_BE_HIGHER_THAN_125)),
      attribute(x -> x.values().sum(), equalTo(100, MUST_BE_EQUAL_TO_100))
    );
  }

  public static Either<Seq<ValidationError>, CardPoints> resultOf(Map<String, String> rawCardPoints) {
    if (rawCardPoints == null) {
      return Either.left(List.of(MUST_NOT_BE_NULL.get()));
    }

    Try<Map<Either<Seq<ValidationError>, Id>, Integer>> cardPointsInput = Try.of(
      () -> rawCardPoints.map((id, points) -> Tuple.of(Id.resultOf(id), Integer.parseInt(points)))
    );
    if (cardPointsInput.isFailure()) {
      return Either.left(List.of(MUST_HAVE_VALID_POINTS.get()));
    }
    if (cardPointsInput.get().exists(idAndPoints -> idAndPoints._1.isLeft())) {
      return Either.left(List.of(MUST_HAVE_VALID_IDS.get()));
    }

    return validation().applyTo(cardPointsInput.get().mapKeys(Either::get)).map(CardPoints::new);
  }

  public Map<Id, Integer> values() {
    return values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CardPoints that = (CardPoints) o;

    return values.equals(that.values);
  }

  @Override
  public int hashCode() {
    return values.hashCode();
  }
}

class CardPointsValidationErrors {

  static final Supplier<ValidationError> MUST_HAVE_VALID_IDS = () -> ValidationError.of(
    CardPoints.class.getName(), "must have valid ids"
  );

  static final Supplier<ValidationError> MUST_HAVE_VALID_POINTS = () -> ValidationError.of(
    CardPoints.class.getName(), "must have valid ids"
  );

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    CardPoints.class.getName(), "must not be null"
  );

  static final Supplier<ValidationError> MUST_BE_DEFINED_FOR_BOTH_TEAMS = () -> ValidationError.of(
    CardPoints.class.getName(), "both teams must have card points"
  );

  static final Supplier<ValidationError> MUST_BE_DIVISIBLE_BY_5 = () -> ValidationError.of(
    CardPoints.class.getName(), "the total of card points must be divisible by 5"
  );

  static final Supplier<ValidationError> MUST_NOT_BE_SMALLER_THAN_MINUS_25 = () -> ValidationError.of(
    CardPoints.class.getName(), "the total of card points of a team can not be smaller than -25"
  );

  static final Supplier<ValidationError> MUST_NOT_BE_HIGHER_THAN_125 = () -> ValidationError.of(
    CardPoints.class.getName(), "the total of card points of a team can not be larger than 125"
  );

  static final Supplier<ValidationError> MUST_BE_EQUAL_TO_100 = () -> ValidationError.of(
    CardPoints.class.getName(), "the sum of card points must be equal to 100"
  );
}
