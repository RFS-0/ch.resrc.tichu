package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.value_objects.*;

import java.util.function.*;

public class RoundSpec {

    private RoundNumber roundNumber;
    private CardPoints cardPoints;
    private Ranks ranks;
    private Tichus tichus;

    private RoundSpec() {
    }

    private RoundSpec(RoundSpec other) {
        this.roundNumber = other.roundNumber;
        this.cardPoints = other.cardPoints;
        this.ranks = other.ranks;
        this.tichus = other.tichus;
    }

    private RoundSpec copied(Consumer<RoundSpec> modification) {
        var copy = new RoundSpec(this);
        modification.accept(copy);
        return copy;
    }

    public RoundSpec roundNumber(int roundNumber) {
        return copied(but -> but.roundNumber = RoundNumber.of(roundNumber));
    }

    public RoundSpec cardPoints(CardPointsSpec spec) {
        return copied(but -> but.cardPoints = CardPoints.of(spec.teamIdToPoints()));
    }

    public RoundSpec ranks(RanksSpec spec) {
        return copied(but -> but.ranks = Ranks.of(spec.playerIdToRank()));
    }
}
