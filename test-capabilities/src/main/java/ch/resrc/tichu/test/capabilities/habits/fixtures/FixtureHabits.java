package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.capabilities.error_handling.faults.*;
import ch.resrc.tichu.test.capabilities.testbed.*;

public interface FixtureHabits {

    default FixtureSupport fixtureSupport() {
        throw Defect.of("This fixture habit needs a " + FixtureSupport.class.getSimpleName() + " instance to run.");
    }
}
