package ch.resrc.old.capabilities.errorhandling;

import java.util.*;
import java.util.function.*;

import static ch.resrc.old.capabilities.errorhandling.ProblemDiagnosis.*;

public class DomainProblemDetected extends ProblemDetected {

  private DomainProblemDetected(ProblemDiagnosis diagnosed) {

    super(diagnosed);
  }

  private DomainProblemDetected(DomainProblemDetected other) {
    super(other);
  }

  public static Supplier<ProblemDetected> supplierFor(Problem problem) {
    return () -> of(problem);
  }

  public static DomainProblemDetected of(DomainProblem detected) {
    return DomainProblemDetected.of(aProblemDiagnosis().withProblem(detected));
  }

  public static DomainProblemDetected of(ProblemDiagnosis diagnosis) {
    return new DomainProblemDetected(diagnosis);
  }

  public static DomainProblemDetected of(List<ProblemDiagnosis> problems) {
    return DomainProblemDetected.of(problems.get(0));
  }

  public static DomainProblemDetected ofErrorChoices(List<? extends ProblemChoice> problems) {
    return of(problems.get(0).diagnosis());
  }

  @Override
  protected DomainProblemDetected copy() {
    return new DomainProblemDetected(this);
  }
}
