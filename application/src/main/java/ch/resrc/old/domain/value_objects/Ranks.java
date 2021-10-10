package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Ranks {

  private final Map<Id, Integer> values;

  private Ranks(Map<Id, Integer> values) {
    this.values = values;
  }

  private static Validation<Seq<ValidationError>, Map<Id, Integer>> validation() {
    return allOf(
      attribute((Map<Id, Integer> input) -> input.keySet().length(), equalTo(4, errorDetails("four players must have a rank"))),
      attribute(x -> x.values().toList().sorted(), distinct(errorDetails("each player must have a distinct rank"))),
      attribute(x -> x.values().toList().sorted(), allMin(1, errorDetails("a rank can not be smaller than one"))),
      attribute(x -> x.values().toList().sorted(), allMax(4, errorDetails("a rank can not be higher than four")))
    );
  }

  public static Either<Seq<ValidationError>, Ranks> resultOf(Map<Id, Integer> values) {
    if (values == null || values.keySet().exists(Objects::isNull) || values.values().exists(Objects::isNull)) {
      return Either.left(List.of(mustNotBeNull().apply(values)));
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
