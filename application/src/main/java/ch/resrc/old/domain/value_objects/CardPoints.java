package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import io.vavr.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class CardPoints {

  private final Map<Id, Integer> values;

  private CardPoints(Map<Id, Integer> values) {
    this.values = values;
  }

  private static Validation<Seq<ValidationError>, Map<Id, Integer>> validation() {
    return allOf(
      attribute(x -> x.keySet().length(), equalTo(2, errorDetails("both teams must have card points"))),
      attribute(x -> x.values().toList().sorted(), allDivisibleBy(5, errorDetails("the total of card points must be divisible by 5"))),
      attribute(x -> x.values().toList().sorted(), allMin(-25, errorDetails("the total of card points of a team can not be smaller than -25"))),
      attribute(x -> x.values().toList().sorted(), allMax(125, errorDetails("the total of card points of a team can not be larger than 125"))),
      attribute(x -> x.values().sum(), equalTo(100, errorDetails("the sum of card points must be equal to 100")))
    );
  }

  Integer ofTeam(Id teamId) {
    return values.get(teamId).getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
  }

  public static Either<Seq<ValidationError>, CardPoints> resultOfRaw(Map<String, String> rawCardPoints) {
    if (rawCardPoints == null) {
      return Either.left(List.of(mustNotBeNull().apply(rawCardPoints)));
    }

    Try<Map<Either<Seq<ValidationError>, Id>, Integer>> cardPointsInput = Try.of(
      () -> rawCardPoints.map((id, points) -> Tuple.of(Id.resultOf(id), Integer.parseInt(points)))
    );
    if (cardPointsInput.isFailure()) {
      return Either.left(List.of(errorDetails("must have valid points").apply(rawCardPoints.values())));
    }
    if (cardPointsInput.get().exists(idAndPoints -> idAndPoints._1.isLeft())) {
      return Either.left(List.of(errorDetails("must have valid ids").apply(rawCardPoints.keySet())));
    }

    return validation().applyTo(cardPointsInput.get().mapKeys(Either::get)).map(CardPoints::new);
  }

  public static Either<Seq<ValidationError>, CardPoints> resultOf(Map<Id, Integer> cardPoints) {
    if (cardPoints == null) {
      return Either.left(List.of(mustNotBeNull().apply(cardPoints)));
    }
    return validation().applyTo(cardPoints).map(CardPoints::new);
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
