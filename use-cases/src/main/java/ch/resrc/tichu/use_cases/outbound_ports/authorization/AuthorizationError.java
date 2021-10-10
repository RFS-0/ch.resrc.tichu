package ch.resrc.tichu.use_cases.outbound_ports.authorization;

import ch.resrc.tichu.capabilities.error_handling.*;

import java.util.*;

/**
 * Represents a possible error that an {@link AccessControl} might return.
 */
public class AuthorizationError extends ProblemEnumeration {

    public AuthorizationError(ProblemDiagnosis signaledProblem) { super(signaledProblem); }

    @Override
    protected List<Problem> possibleProblems() {

        return List.of(AuthorizationProblem.ACCESS_DENIED);
    }
}
