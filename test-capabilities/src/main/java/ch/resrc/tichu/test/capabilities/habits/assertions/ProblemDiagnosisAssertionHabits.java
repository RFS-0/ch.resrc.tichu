package ch.resrc.tichu.test.capabilities.habits.assertions;

import ch.resrc.tichu.capabilities.error_handling.*;
import org.assertj.core.api.*;

import java.util.*;
import java.util.stream.*;

public interface ProblemDiagnosisAssertionHabits {

    default Condition<ProblemDiagnosis> contextContaining(String key, Object expected) {

        return new Condition<>((ProblemDiagnosis diagnosis) ->
                diagnosis.context().containsKey(key)
                        && expected.toString().equals(diagnosis.context().get(key).toString()),
                "should have context containing <%s=%s>", key, expected);
    }

    default Condition<ProblemDiagnosis> contextValuesFor(String... keys) {

        return new Condition<>(
                (ProblemDiagnosis diagnosis) -> Stream.of(keys)
                        .noneMatch(x -> !diagnosis.context().containsKey(x)
                                || diagnosis.context().get(x) == null)

                , "should have context containing values for keys: <%s>", List.of(keys)
        );
    }
}
