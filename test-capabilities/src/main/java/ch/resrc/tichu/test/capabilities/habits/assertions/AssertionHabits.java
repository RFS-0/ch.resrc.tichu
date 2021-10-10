package ch.resrc.tichu.test.capabilities.habits.assertions;

import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.test.capabilities.assertions.*;
import org.assertj.core.api.*;
import org.hamcrest.*;

import static ch.resrc.tichu.test.capabilities.assertions.EventsAssert.*;

public class AssertionHabits extends Assertions {

    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {

        MatcherAssert.assertThat(actual, matcher);
    }

    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {

        MatcherAssert.assertThat(reason, actual, matcher);
    }

    public static <T, E> ResultAssert<T, E> assertThat(Result<T, E> actualResult) {

        return ResultAssert.assertThatResult(actualResult);
    }

    public static EventsAssert assertThat(Events events) {

        return assertThatEvents(events);
    }
}
