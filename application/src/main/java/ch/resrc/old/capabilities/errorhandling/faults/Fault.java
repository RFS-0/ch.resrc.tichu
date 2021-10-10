package ch.resrc.old.capabilities.errorhandling.faults;

import ch.resrc.old.capabilities.errorhandling.*;

public abstract class Fault extends ProblemDetected {

  Fault(Fault other) {
    super(other);
  }

  Fault(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

}
