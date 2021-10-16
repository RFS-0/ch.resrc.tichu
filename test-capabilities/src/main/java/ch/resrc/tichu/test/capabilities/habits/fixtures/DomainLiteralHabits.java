package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.value_objects.*;

public interface DomainLiteralHabits {

    DomainLiteralHabits domainLiterals = new DomainLiteralHabits() {
    };

    default ClientId clientId(String literal) {
        return ClientId.of(literal);
    }

    default Email email(String literal) {
        return Email.of(literal);
    }

    default Id id(String id) {
        return Id.of(id);
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

    default Tichu tichu(int literal) {
        return Tichu.of(literal);
    }
}
