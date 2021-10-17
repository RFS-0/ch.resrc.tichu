package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.*;
import io.vavr.collection.*;

import java.util.function.*;

public class RanksSpec {

    private String firstPlayerId;
    private String secondPlayerId;
    private String thirdPlayerId;
    private String fourthPlayerId;
    private int rankOfFirstPlayer;
    private int rankOfSecondPlayer;
    private int rankOfThirdPlayer;
    private int rankOfFourthPlayer;

    private RanksSpec() {
    }

    public RanksSpec(RanksSpec other) {
        this.firstPlayerId = other.firstPlayerId;
        this.secondPlayerId = other.secondPlayerId;
        this.thirdPlayerId = other.thirdPlayerId;
        this.fourthPlayerId = other.fourthPlayerId;
        this.rankOfFirstPlayer = other.rankOfFirstPlayer;
        this.rankOfSecondPlayer = other.rankOfSecondPlayer;
        this.rankOfThirdPlayer = other.rankOfThirdPlayer;
        this.rankOfFourthPlayer = other.rankOfFourthPlayer;
    }

    private RanksSpec copied(Consumer<RanksSpec> modification) {
        var copy = new RanksSpec(this);
        modification.accept(copy);
        return copy;
    }

    public RanksSpec firstPlayerId(String firstPlayerId) {
        return copied(but -> but.firstPlayerId = firstPlayerId);
    }

    public RanksSpec secondPlayerId(String secondPlayerId) {
        return copied(but -> but.secondPlayerId = secondPlayerId);
    }

    public RanksSpec thirdPlayerId(String thirdPlayerId) {
        return copied(but -> but.thirdPlayerId = thirdPlayerId);
    }

    public RanksSpec fourthPlayerId(String fourthPlayerId) {
        return copied(but -> but.fourthPlayerId = fourthPlayerId);
    }

    public RanksSpec rankOfFirstPlayer(int rank) {
        return copied(but -> but.rankOfFirstPlayer = rank);
    }

    public RanksSpec rankOfFirstSecond(int rank) {
        return copied(but -> but.rankOfFirstPlayer = rank);
    }

    public RanksSpec rankOfFirstThird(int rank) {
        return copied(but -> but.rankOfFirstPlayer = rank);
    }

    public RanksSpec rankOfFirstFourth(int rank) {
        return copied(but -> but.rankOfFirstPlayer = rank);
    }

    public Map<PlayerId, Rank> playerIdToRank() {
        return HashMap.of(
                PlayerId.of(firstPlayerId), Rank.of(rankOfFirstPlayer),
                PlayerId.of(secondPlayerId), Rank.of(rankOfSecondPlayer),
                PlayerId.of(thirdPlayerId), Rank.of(rankOfThirdPlayer),
                PlayerId.of(fourthPlayerId), Rank.of(rankOfFourthPlayer)
        );
    }
}
