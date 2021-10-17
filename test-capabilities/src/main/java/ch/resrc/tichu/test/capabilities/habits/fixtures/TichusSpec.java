package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.*;
import io.vavr.collection.*;

import java.util.function.*;

public class TichusSpec {

    private String firstPlayerId;
    private String secondPlayerId;
    private String thirdPlayerId;
    private String fourthPlayerId;
    private int tichuOfFirstPlayer;
    private int tichuOfSecondPlayer;
    private int tichuOfThirdPlayer;
    private int tichuOfFourthPlayer;

    private TichusSpec() {
    }

    public TichusSpec(TichusSpec other) {
        this.firstPlayerId = other.firstPlayerId;
        this.secondPlayerId = other.secondPlayerId;
        this.thirdPlayerId = other.thirdPlayerId;
        this.fourthPlayerId = other.fourthPlayerId;
        this.tichuOfFirstPlayer = other.tichuOfFirstPlayer;
        this.tichuOfSecondPlayer = other.tichuOfSecondPlayer;
        this.tichuOfThirdPlayer = other.tichuOfThirdPlayer;
        this.tichuOfFourthPlayer = other.tichuOfFourthPlayer;
    }

    private TichusSpec copied(Consumer<TichusSpec> modification) {
        var copy = new TichusSpec(this);
        modification.accept(copy);
        return copy;
    }

    public TichusSpec firstPlayerId(String firstPlayerId) {
        return copied(but -> but.firstPlayerId = firstPlayerId);
    }

    public TichusSpec secondPlayerId(String secondPlayerId) {
        return copied(but -> but.secondPlayerId = secondPlayerId);
    }

    public TichusSpec thirdPlayerId(String thirdPlayerId) {
        return copied(but -> but.thirdPlayerId = thirdPlayerId);
    }

    public TichusSpec fourthPlayerId(String fourthPlayerId) {
        return copied(but -> but.fourthPlayerId = fourthPlayerId);
    }

    public TichusSpec tichuOfFirstPlayer(int tichu) {
        return copied(but -> but.tichuOfFirstPlayer = tichu);
    }

    public TichusSpec tichuOfSecondPlayer(int tichu) {
        return copied(but -> but.tichuOfSecondPlayer = tichu);
    }

    public TichusSpec tichuOfThirdPlayer(int tichu) {
        return copied(but -> but.tichuOfThirdPlayer = tichu);
    }

    public TichusSpec tichuOfFourthPlayer(int tichu) {
        return copied(but -> but.tichuOfFourthPlayer = tichu);
    }

    public String firstPlayerId() {
        return firstPlayerId;
    }

    public String secondPlayerId() {
        return secondPlayerId;
    }

    public String thirdPlayerId() {
        return thirdPlayerId;
    }

    public String fourthPlayerId() {
        return fourthPlayerId;
    }

    public int tichuOfFirstPlayer() {
        return tichuOfFirstPlayer;
    }

    public int tichuOfSecondPlayer() {
        return tichuOfSecondPlayer;
    }

    public int tichuOfThirdPlayer() {
        return tichuOfThirdPlayer;
    }

    public int tichuOfFourthPlayer() {
        return tichuOfFourthPlayer;
    }

    public Map<PlayerId, Tichu> playerIdToTichu() {
        return HashMap.of(
                PlayerId.of(firstPlayerId), Tichu.of(tichuOfFirstPlayer),
                PlayerId.of(secondPlayerId), Tichu.of(tichuOfSecondPlayer),
                PlayerId.of(thirdPlayerId), Tichu.of(tichuOfThirdPlayer),
                PlayerId.of(fourthPlayerId), Tichu.of(tichuOfFourthPlayer)
        );
    }
}
