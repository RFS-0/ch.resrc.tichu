package ch.resrc.old.capabilities.errorhandling;

/**
 * Implementations signal a {@link Problem} and are able to provide a {@link ProblemDiagnosis} for the problem.
 */
public interface HavingDiagnosis {

  /**
   * @return the {@link ProblemDiagnosis} that describes the signaled problem.
   */
  ProblemDiagnosis diagnosis();
}
