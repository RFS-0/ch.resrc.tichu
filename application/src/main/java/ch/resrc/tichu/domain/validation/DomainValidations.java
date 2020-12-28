package ch.resrc.tichu.domain.validation;

import static java.util.stream.Collectors.toList;

import ch.resrc.tichu.capabilities.errorhandling.DomainProblem;
import ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import java.util.List;
import java.util.function.Function;

public class DomainValidations {

  public static Function<ValidationError, ProblemDiagnosis> mandatoryPropertyMissing() {
    return (ValidationError error) -> ProblemDiagnosis.of(DomainProblem.MANDATORY_VALUE_MISSING)
      .withContext("message", error.errorMessage());
  }

  public static Function<List<ValidationError>, DomainProblemDetected> invariantViolated() {
    return (List<ValidationError> errors) -> {
      var problems = errors.stream()
        .map(error -> ProblemDiagnosis.of(DomainProblem.INVARIANT_VIOLATED)
          .withContext("message", error.errorMessage())
        )
        .collect(toList());

      return DomainProblemDetected.of(problems);
    };
  }
}
