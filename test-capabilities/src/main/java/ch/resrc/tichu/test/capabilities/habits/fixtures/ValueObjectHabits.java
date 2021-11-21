package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.value_objects.*;

public interface ValueObjectHabits {

    ValueObjectHabits valueObjects = new ValueObjectHabits() {
    };

    default ClientId clientId(String literal) {
        return ClientId.of(literal);
    }

    default Email email(String literal) {
        return Email.of(literal);
    }

    default JoinCode joinCode(String literal) {
        return JoinCode.of(literal);
    }

    default Name name(String being) {

        return Name.of(being);
    }

    default NamespaceId namespaceId(String being) {
        return NamespaceId.of(being);
    }

    default Picture picture(String literal) {
        return Picture.of(literal);
    }

    default Rank rank(int literal) {
        return Rank.of(literal);
    }

    default RoundNumber roundNumber(int literal) {
        return RoundNumber.of(literal);
    }

    default CardPoints cardPoints(String firstTeamId, String secondTeamId, int pointsOfFirstTeam, int pointsOfSecondTeam) {
        return CardPointsSpec.cardPoints()
                .firstTeamId(firstTeamId)
                .pointsOfFirstTeam(pointsOfFirstTeam)
                .secondTeamId(secondTeamId)
                .pointsOfSecondTeam(pointsOfSecondTeam)
                .asValueObject();
    }

    default Round round(int roundNumber, CardPointsSpec cardPoints, RanksSpec ranks, TichusSpec tichus) {
        return RoundSpec.round()
                .roundNumber(roundNumber)
                .cardPoints(cardPoints)
                .ranks(ranks)
                .tichus(tichus)
                .asValueObject();
    }

    default Tichu tichu(int literal) {
        return Tichu.of(literal);
    }
}
