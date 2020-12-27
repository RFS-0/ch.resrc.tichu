package ch.resrc.tichu.capabilities.errorhandling;

import java.util.List;

public abstract class DomainError extends ProblemChoice implements BusinessError {

  protected DomainError(ProblemDiagnosis diagnosis) {
    super(diagnosis);
  }

  @Override
  public DomainProblemDetected asException() {
    return DomainProblemDetected.of(this.diagnosis());
  }

  public static None none(ProblemDiagnosis diagnosis) {
    return new None(diagnosis);
  }

  public static class None extends DomainError {

    public None(ProblemDiagnosis diagnosis) {
      super(diagnosis);
    }

    @Override
    protected List<Problem> choices() {
      return List.of();
    }
  }
}
