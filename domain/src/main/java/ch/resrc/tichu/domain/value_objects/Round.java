package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.errorhandling.*;
import io.vavr.*;
import io.vavr.collection.*;

import java.util.function.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class Round implements Comparable<Round> {

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

    private static Validation<Round, ValidationError> validation() {
        return modified(
                allOf(
                        attribute(x -> x.roundNumber, notNull()),
                        attribute(x -> x.cardPoints, notNull()),
                        attribute(x -> x.ranks, notNull()),
                        attribute(x -> x.tichus, notNull())
                ),
                context(Round.class)
        );
    }

    public static Result<Round, ValidationError> resultOf(RoundNumber roundNumber,
                                                          CardPoints cardPoints,
                                                          Ranks ranks,
                                                          Tichus tichus) {
        return validation().applyTo(new Round(roundNumber, cardPoints, ranks, tichus));
    }

    @Override
    public int compareTo(Round other) {
        return this.roundNumber.compareTo(other.roundNumber);
    }

    public boolean doubleVictory(Id firstPlayer, Id secondPlayer) {
        return ranks.rankOfPlayer(firstPlayer).value() + ranks.rankOfPlayer(secondPlayer).value() == 3;
    }

    public int totalPoints(Id teamId, Id firstPlayer, Id secondPlayer) {
        return cardPoints(teamId)
                + matchPoints(firstPlayer, secondPlayer)
                + tichuPoints(firstPlayer)
                + tichuPoints(secondPlayer);
    }

    private int cardPoints(Id team) {
        return cardPoints.ofTeam(team).getOrElseThrow(
                () -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED)
        );
    }

    private int matchPoints(Id firstPlayer, Id secondPlayer) {
        return doubleVictory(firstPlayer, secondPlayer) ? 100 : 0;
    }

    private int tichuPoints(Id playerId) {
        return tichus.value().get(playerId)
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
        Tichus evaluatedTichus = evaluateTichus().getOrThrow(invariantViolated());
        return butTichus(evaluatedTichus);
    }

    private Result<Tichus, ValidationError> evaluateTichus() {
        Map<Id, Tichu> evaluatedTichus = tichus.value().map((playerId, tichu) -> {
            boolean firstPlace = ranks.rankOfPlayer(playerId) == Rank.FIRST;

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

        return Tichus.resultOf(evaluatedTichus);
    }
}

