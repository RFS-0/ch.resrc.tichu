package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.*;

import java.time.*;

public interface EntityHabits {

    ValueObjectHabits entities = new ValueObjectHabits() {
    };

    default User user(String id, String name, String email, Instant createdAt) {
        return User.resultOf(
                        UserId.of(id),
                        Name.of(name),
                        Email.of(email),
                        createdAt
                )
                .value();
    }

    default Player player(String id, String name, Instant createdAt) {
        return Player.resultOf(
                        PlayerId.of(id),
                        Name.of(name),
                        createdAt
                )
                .value();
    }

    default Team team(String id) {
        return Team.resultOf(TeamId.of(id)).value();
    }
}
