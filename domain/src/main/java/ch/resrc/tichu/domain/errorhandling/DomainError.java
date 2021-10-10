package ch.resrc.tichu.domain.errorhandling;

import ch.resrc.tichu.capabilities.error_handling.*;

import java.util.*;

public abstract class DomainError extends ProblemEnumeration implements BusinessError {

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
        protected List<Problem> possibleProblems() {
            return List.of();
        }
    }
}
