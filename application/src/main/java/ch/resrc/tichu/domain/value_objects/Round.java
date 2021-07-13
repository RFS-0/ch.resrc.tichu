package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.errorhandling.DomainProblem;
import ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.Tuple;
import io.vavr.collection.Map;
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
  private Tichus tichus;

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

  public boolean match(Id firstPlayer, Id secondPlayer) {
    return ranks.values()
      .filter((playerId, rank) -> playerId.equals(firstPlayer) || playerId.equals(secondPlayer))
      .map((idAndRank) -> idAndRank._2)
      .sum().intValue() == 3;
  }

  public int totalPoints(Id teamId, Id firstPlayer, Id secondPlayer) {
    return cardPoints(teamId)
      + matchPoints(firstPlayer, secondPlayer)
      + tichuPoints(firstPlayer)
      + tichuPoints(secondPlayer);
  }

  private int cardPoints(Id team) {
    return cardPoints.ofTeam(team);
  }

  private int matchPoints(Id firstPlayer, Id secondPlayer) {
    return match(firstPlayer, secondPlayer) ? 100 : 0;
  }

  private int tichuPoints(Id playerId) {
    return tichus.values().get(playerId)
      .map(Tichu::value)
      .getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
  }

  public RoundNumber roundNumber() {
    return roundNumber;
  }

  public CardPoints cardPoints() {
    return cardPoints;
  }

  public Round butCardPoints(CardPoints cardPoints) {
    return copied(but -> but.cardPoints = cardPoints);
  }

  public Ranks ranks() {
    return ranks;
  }

  public Round butRanks(Ranks ranks) {
    return copied(but -> but.ranks = ranks);
  }

  public Tichus tichus() {
    return tichus;
  }

  public Round butTichus(Tichus tichus) {
    return copied(but -> but.tichus = tichus);
  }

  public Round finish() {
    Tichus evaluatedTichus = evaluateTichus();
    return butTichus(evaluatedTichus);
  }

  private Tichus evaluateTichus() {
    Map<Id, Tichu> evaluatedTichus = tichus.values()
      .map((playerId, tichu) -> {
        Boolean firstPlace = ranks.values()
          .get(playerId)
          .map(rank -> rank <= 1)
          .getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));

        Tichu evaluatedTichu = switch (tichu) {
          case TICHU_CALLED -> {
            if (firstPlace) {
              yield Tichu.TICHU_SUCCEEDED;
            } else {
              yield Tichu.TICHU_FAILED;
            }
          }
          case GRAND_TICHU_CALLED -> {
            if (firstPlace) {
              yield Tichu.GRAND_TICHU_SUCCEEDED;
            } else {
              yield Tichu.GRAND_TICHU_FAILED;
            }
          }
          default -> Tichu.NONE;
        };
        return Tuple.of(playerId, evaluatedTichu);
      });
    return Tichus.resultOf(evaluatedTichus)
      .getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
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

