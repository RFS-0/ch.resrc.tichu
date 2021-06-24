package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class Round {

  private final RoundNumber roundNumber;
  private CardPoints cardPoints;
  private Ranks ranks;
  private final Tichus tichus;

  private Round(RoundNumber roundNumber, CardPoints cardPoints, Ranks ranks, Tichus tichus) {
    this.roundNumber = roundNumber;
    this.cardPoints = cardPoints;
    this.ranks = ranks;
    this.tichus = tichus;
  }

  private Round(Round other) {
    this.roundNumber = other.roundNumber;
    this.cardPoints = other.cardPoints;
    this.ranks = other.ranks;
    this.tichus = other.tichus;
  }

  public Round copied(Consumer<Round> modification) {
    var theCopy = new Round(this);
    modification.accept(theCopy);
    return theCopy;
  }

  private static Validation<Seq<ValidationError>, Round> validation() {
    return allOf(
      attribute(x -> x.roundNumber, notNull(mustNotBeNull())),
      attribute(x -> x.cardPoints, notNull(mustNotBeNull())),
      attribute(x -> x.ranks, notNull(mustNotBeNull())),
      attribute(x -> x.tichus, notNull(mustNotBeNull()))
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

  public Round butRanks(Ranks ranks) {
    return copied(but -> but.ranks = ranks);
  }

  public Round butCardPoints(CardPoints cardPoints) {
    return copied(but -> but.cardPoints = cardPoints);
  }

  public Tichus tichus() {
    return tichus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Round round = (Round) o;

    return roundNumber.equals(round.roundNumber);
  }

  @Override
  public int hashCode() {
    return roundNumber.hashCode();
  }
}

