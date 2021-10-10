package ch.resrc.tichu.domain.validation;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.errorhandling.*;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.*;

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
