package ch.resrc.tichu.domain.errorhandling;

import ch.resrc.tichu.capabilities.error_handling.*;

import java.util.*;

public class AuthorizationError extends DomainError {

    public AuthorizationError(ProblemDiagnosis diagnosis) {
        super(diagnosis);
    }

    @Override
    protected List<Problem> possibleProblems() {

        return List.of();
    }
}
