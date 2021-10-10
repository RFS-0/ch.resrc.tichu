package ch.resrc.tichu.test.capabilities.assertions;

import ch.resrc.tichu.capabilities.error_handling.*;
import org.assertj.core.api.*;

import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.AssertionHabits.*;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class TryAssert<T> extends AbstractAssert<TryAssert<T>, Try<T>> {

    private TryAssert(Try<T> actual) {
        super(actual, TryAssert.class);
    }

    public static <U> TryAssert<U> assertThatTry(Try<U> actual) {
        return new TryAssert<>(actual);
    }

    public TryAssert<T> isSuccess() {
        actual.mapEmptyToFailure(NoSuchElementException::new)
              .onFailure(bad -> failWithMessage("Expected success but was failure: <%s>", bad.getMessage()));

        return this;
    }

    public TryAssert<T> isFailure() {
        if (!actual.isFailure()) {
            failWithMessage("Expected result to be a failure but wasn't");
        }

        return this;
    }

    public TryAssert<T> hasValueSatisfying(Consumer<T> requirements) {
        isSuccess();
        return satisfies(actual -> actual.onSuccess(requirements));
    }


    public <X extends RuntimeException> TryAssert<T> hasFailureSatisfying(Class<X> expected, Consumer<X> requirements) {
        isFailure();
        actual.onFailure(bad -> assertThat(bad).isInstanceOfSatisfying(expected, requirements));

        return this;
    }

    public <X extends RuntimeException> TryAssert hasFailure(Class<X> expected) {
        isFailure();
        actual.onFailure(bad -> assertThat(bad).isInstanceOf(expected));

        return this;
    }


}
