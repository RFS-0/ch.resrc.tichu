package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.test.capabilities.habits.use_cases.*;

public interface UserFixtureHabits extends
        PortsHabits,
        FixtureHabits,
        DomainLiteralHabits {

    default UserSpec aUser() {

        return new UserSpec();
    }
}
