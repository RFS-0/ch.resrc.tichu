package ch.resrc.tichu.domain.errorhandling;

import ch.resrc.tichu.capabilities.error_handling.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.error_handling.ProblemDiagnosis.*;

public class DomainProblemDetected extends ProblemDetected {

    private DomainProblemDetected(ProblemDiagnosis diagnosed) {

        super(diagnosed);
    }

    private DomainProblemDetected(DomainProblemDetected other) {

        super(other);
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

    public static DomainProblemDetected ofErrorChoices(List<? extends ProblemEnumeration> problems) {

        return DomainProblemDetected.of(problems.get(0).diagnosis());
    }

    @Override
    protected DomainProblemDetected copy() {

        return new DomainProblemDetected(this);
    }
}
