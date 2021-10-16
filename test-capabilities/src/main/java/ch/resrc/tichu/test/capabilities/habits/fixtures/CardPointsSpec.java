package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.value_objects.*;
import io.vavr.collection.*;

import java.util.function.*;

public class CardPointsSpec {

    private Id firstTeamId;
    private Id secondTeamId;
    private int pointsOfFirstTeam;
    private int pointsOfSecondTeam;

    private CardPointsSpec() {
    }

    private CardPointsSpec(CardPointsSpec other) {
        this.firstTeamId = other.firstTeamId;
        this.secondTeamId = other.secondTeamId;
        this.pointsOfFirstTeam = other.pointsOfFirstTeam;
        this.pointsOfSecondTeam = other.pointsOfSecondTeam;
    }

    private CardPointsSpec copied(Consumer<CardPointsSpec> modification) {
        var copy = new CardPointsSpec(this);
        modification.accept(copy);
        return copy;
    }

    public static CardPointsSpec cardPoints() {
        return new CardPointsSpec();
    }

    public CardPointsSpec firstTeamId(String firstTeamId) {
        return copied(but -> but.firstTeamId = Id.of(firstTeamId));
    }

    public CardPointsSpec pointsOfFirstTeam(int pointsOfFirstTeam) {
        return copied(but -> but.pointsOfFirstTeam = pointsOfFirstTeam);
    }

    public CardPointsSpec secondTeamId(String secondTeamId) {
        return copied(but -> but.secondTeamId = Id.of(secondTeamId));
    }

    public CardPointsSpec pointsOfSecondTeam(int pointsOfSecondTeam) {
        return copied(but -> but.pointsOfSecondTeam = pointsOfSecondTeam);
    }

    public Id firstTeamId() {
        return firstTeamId;
    }

    public Id secondTeamId() {
        return secondTeamId;
    }

    public int pointsOfFirstTeam() {
        return pointsOfFirstTeam;
    }

    public int pointsOfSecondTeam() {
        return pointsOfSecondTeam;
    }

    public Map<Id, Integer> teamIdToPoints() {
        return HashMap.of(
                firstTeamId, pointsOfFirstTeam,
                secondTeamId, pointsOfSecondTeam
        );
    }

    CardPoints asValueObject() {
        return CardPoints.of(teamIdToPoints());
    }
}
