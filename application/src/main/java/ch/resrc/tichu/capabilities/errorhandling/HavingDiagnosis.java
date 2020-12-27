package ch.resrc.tichu.capabilities.errorhandling;

import java.util.function.Function;

/**
 * Implementations signal a {@link Problem} and are able to provide a {@link ProblemDiagnosis} for the problem.
 */
public interface HavingDiagnosis {

  /**
   * @return the {@link ProblemDiagnosis} that describes the signaled problem.
   */
  ProblemDiagnosis diagnosis();


  static <E extends HavingDiagnosis> Function<E, ProblemDiagnosis> toDiagnosis() {
    return HavingDiagnosis::diagnosis;
  }
}
