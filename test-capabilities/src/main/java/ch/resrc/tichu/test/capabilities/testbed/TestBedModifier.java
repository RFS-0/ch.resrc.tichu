package ch.resrc.tichu.test.capabilities.testbed;

import java.util.function.*;

/**
 * Modifies the configuration of the test bed that gest passed to the accept method.
 * <p>
 * Using functions of this type, we can change the test bed configuration
 * in tests in a declarative way.
 * </p>
 */
public interface TestBedModifier extends Consumer<TestBed> {
}
