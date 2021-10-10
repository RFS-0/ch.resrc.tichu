package ch.resrc.tichu.capabilities.error_handling.faults;

import ch.resrc.tichu.capabilities.error_handling.*;

public abstract class Fault extends ProblemDetected {

    Fault(Fault other) {
        super(other);
    }

    Fault(ProblemDiagnosis diagnosed) {
        super(diagnosed);
    }

}
