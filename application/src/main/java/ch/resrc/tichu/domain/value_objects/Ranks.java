package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allMax;
import static ch.resrc.tichu.capabilities.validation.Validations.allMin;
import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.distinct;
import static ch.resrc.tichu.capabilities.validation.Validations.equalTo;
import static ch.resrc.tichu.domain.value_objects.RanksValidationErrors.MUST_BE_DEFINED_FOR_FOUR_PLAYERS;
import static ch.resrc.tichu.domain.value_objects.RanksValidationErrors.MUST_BE_DISTINCT_FOR_ALL_PLAYERS;
import static ch.resrc.tichu.domain.value_objects.RanksValidationErrors.MUST_NOT_BE_HIGHER_THAN_FOUR;
import static ch.resrc.tichu.domain.value_objects.RanksValidationErrors.MUST_NOT_BE_NULL;
import static ch.resrc.tichu.domain.value_objects.RanksValidationErrors.MUST_NOT_BE_SMALLER_THAN_ONE;

public class Ranks {

  private final Map<Id, Integer> values;

  private Ranks(Map<Id, Integer> values) {
    this.values = values;
  }

  private static Validation<Seq<ValidationError>, Map<Id, Integer>> validation() {
    return allOf(
      attribute(x -> x.keySet().length(), equalTo(4, MUST_BE_DEFINED_FOR_FOUR_PLAYERS)),
      attribute(Map::values, distinct(MUST_BE_DISTINCT_FOR_ALL_PLAYERS)),
      attribute(Map::values, allMin(1, MUST_NOT_BE_SMALLER_THAN_ONE)),
      attribute(Map::values, allMax(4, MUST_NOT_BE_HIGHER_THAN_FOUR))
    );
  }

  public static Either<Seq<ValidationError>, Ranks> resultOf(Map<Id, Integer> values) {
    if (values == null || values.keySet().exists(Objects::isNull) || values.values().exists(Objects::isNull)) {
      return Either.left(List.of(MUST_NOT_BE_NULL.get()));
    }
    return validation().applyTo(values).map(Ranks::new);
  }

  public Either<Seq<ValidationError>, Ranks> nextRank(Id playerId) {
    return resultOf(values().put(playerId, values.values().max().get() + 1));
  }

  public Either<Seq<ValidationError>, Ranks> resetRank(Id playerId) {
    return resultOf(values().remove(playerId));
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

    Ranks ranks = (Ranks) o;

    return values.equals(ranks.values);
  }

  @Override
  public int hashCode() {
    return values.hashCode();
  }
}

class RanksValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    Ranks.class.getName(), "no null values allowed"
  );

  static final Supplier<ValidationError> MUST_BE_DEFINED_FOR_FOUR_PLAYERS = () -> ValidationError.of(
    Ranks.class.getName(), "four players must have a rank"
  );

  static final Supplier<ValidationError> MUST_BE_DISTINCT_FOR_ALL_PLAYERS = () -> ValidationError.of(
    Ranks.class.getName(), "each player must have a distinct rank"
  );

  static final Supplier<ValidationError> MUST_NOT_BE_SMALLER_THAN_ONE = () -> ValidationError.of(
    Ranks.class.getName(), "a rank can not be smaller than one"
  );

  static final Supplier<ValidationError> MUST_NOT_BE_HIGHER_THAN_FOUR = () -> ValidationError.of(
    Ranks.class.getName(), "a rank can not be higher than four"
  );

}
