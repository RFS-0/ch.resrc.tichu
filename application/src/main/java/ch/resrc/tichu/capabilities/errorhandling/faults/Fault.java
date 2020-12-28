package ch.resrc.tichu.capabilities.errorhandling.faults;

import ch.resrc.tichu.capabilities.errorhandling.ProblemDetected;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;

public abstract class Fault extends ProblemDetected {

  Fault(Fault other) {
    super(other);
  }

  Fault(ProblemDiagnosis diagnosed) {
    super(diagnosed);
  }

}
