package ch.resrc.tichu.test.capabilities.habits.use_cases;

import ch.resrc.tichu.capabilities.error_handling.faults.*;
import ch.resrc.tichu.test.capabilities.testbed.*;

public interface PortsHabits {

    default Ports ports() {
        throw Defect.of("This test habit needs " + Ports.class.getSimpleName() + " to run.");
    }
}
