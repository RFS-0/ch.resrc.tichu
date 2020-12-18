package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.value_objects.RoundValidationErrors.MUST_NOT_BE_NULL;

public class Round {

  private final RoundNumber roundNumber;
  private final CardPoints cardPoints;
  private final Ranks ranks;
  private final Tichus tichus;

  private Round(RoundNumber roundNumber, CardPoints cardPoints, Ranks ranks, Tichus tichus) {
    this.roundNumber = roundNumber;
    this.cardPoints = cardPoints;
    this.ranks = ranks;
    this.tichus = tichus;
  }

  private static Validation<Seq<ValidationError>, Round> validation() {
    return allOf(
      attribute(x -> x.roundNumber, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.cardPoints, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.ranks, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.tichus, notNull(MUST_NOT_BE_NULL))
    );
  }

  public static Either<Seq<ValidationError>, Round> resultOf(RoundNumber roundNumber, CardPoints cardPoints, Ranks ranks, Tichus tichus) {
    return validation().applyTo(new Round(roundNumber, cardPoints, ranks, tichus));
  }

  public RoundNumber roundNumber() {
    return roundNumber;
  }

  public CardPoints cardPoints() {
    return cardPoints;
  }

  public Ranks ranks() {
    return ranks;
  }

  public Tichus tichus() {
    return tichus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Round round = (Round) o;

    if (!roundNumber.equals(round.roundNumber)) {
      return false;
    }
    if (!cardPoints.equals(round.cardPoints)) {
      return false;
    }
    if (!ranks.equals(round.ranks)) {
      return false;
    }
    return tichus.equals(round.tichus);
  }

  @Override
  public int hashCode() {
    int result = roundNumber.hashCode();
    result = 31 * result + cardPoints.hashCode();
    result = 31 * result + ranks.hashCode();
    result = 31 * result + tichus.hashCode();
    return result;
  }
}

class RoundValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    Round.class.getName(), "must not be null"
  );
}
