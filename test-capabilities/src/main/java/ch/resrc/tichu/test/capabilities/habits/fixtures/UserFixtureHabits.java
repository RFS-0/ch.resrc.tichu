package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.test.capabilities.habits.use_cases.PortsHabits;

import java.time.Instant;

public interface UserFixtureHabits extends
        PortsHabits,
        FixtureHabits,
        ValueObjectHabits {

    default UserSpec aUser() {
        return UserSpec.user()
                .id(aUserId("eff5f160-4b34-4d88-aa7b-0bb14bed06d1"))
                .createdAt(Instant.now())
                .name(Name.of("default-user-name"))
                .email(Email.of("default@email.xyz"));
    }

    default UserId aUserId(String literal) {
        return UserId.of(literal);
    }
}
