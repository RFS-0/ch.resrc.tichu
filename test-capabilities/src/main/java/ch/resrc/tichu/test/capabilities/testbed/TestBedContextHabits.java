package ch.resrc.tichu.test.capabilities.testbed;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import ch.resrc.tichu.test.capabilities.habits.fixtures.*;

/**
 * Provides all the functionality defined by the listed test habit interfaces.
 * Implementations just need to bind the hook methods that the test habits rely on (non-default interface methods).
 */
public interface TestBedContextHabits
        extends
        DomainLiteralHabits,

        EventBusAssertionHabits {
}
