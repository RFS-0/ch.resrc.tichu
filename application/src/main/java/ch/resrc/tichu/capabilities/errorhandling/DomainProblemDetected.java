package ch.resrc.tichu.capabilities.errorhandling;

import java.util.List;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis.aProblemDiagnosis;

public class DomainProblemDetected extends ProblemDetected {

  private DomainProblemDetected(ProblemDiagnosis diagnosed) {

    super(diagnosed);
  }

  private DomainProblemDetected(DomainProblemDetected other) {
    super(other);
  }

  public static Supplier<ProblemDetected> supplierFor(Problem problem) {
    return () -> DomainProblemDetected.of(problem);
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
    return DomainProblemDetected.of(problems.get(0).diagnosis());
  }

  @Override
  protected DomainProblemDetected copy() {
    return new DomainProblemDetected(this);
  }
}
