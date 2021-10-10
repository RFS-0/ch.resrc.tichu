package ch.resrc.tichu.test.capabilities.adapters.testdoubles;

import ch.resrc.tichu.capabilities.error_handling.*;

import java.util.*;

public class FakeProblemCatalogue implements ProblemCatalogue {

    private Map<Problem, ProblemCode> catalogue = new HashMap<>();


    @Override
    public ProblemCode codeFor(Problem problem) {
        return catalogue.getOrDefault(problem, ProblemCode.UNDEFINED);
    }

    public FakeProblemCatalogue map(Problem problem, ProblemCode code) {
        catalogue.put(problem, code);
        return this;
    }
}
