package ch.resrc.tichu.test.capabilities.habits.assertions;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.test.capabilities.adapters.testdoubles.*;
import org.assertj.core.api.Condition;
import org.hamcrest.*;

import java.util.function.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.AssertionHabits.*;
import static ch.resrc.tichu.test.capabilities.habits.assertions.CustomMatchers.instanceOf;
import static ch.resrc.tichu.test.capabilities.habits.assertions.CustomMatchers.*;
import static org.hamcrest.Matchers.*;

public interface EventBusAssertionHabits  {

    default Condition<FakeEventBus> deliveredOnce(Class<? extends Event> expected) {

        return new Condition<>((FakeEventBus eventBus) -> {
            return eventBus.hasDeliveredOnce(expected);
        }, "delivered once an event of type <%s>", expected.getSimpleName());
    }

    default Condition<FakeEventBus> delivered(int expectedFrequency, Class<? extends Event> expected) {

        return new Condition<>((FakeEventBus eventBus) -> {
            return eventBus.findAllDelivered(expected).size() == expectedFrequency;
        }, "delivered <%s> times an event of type <%s>", expectedFrequency, expected.getSimpleName());
    }

    default Condition<FakeEventBus> delivered(Class<? extends Event> expected) {

        return new Condition<>((FakeEventBus eventBus) -> {
            return eventBus.findAllDelivered(expected).size() != 0;
        }, "delivered one or more events of type <%s>", expected.getSimpleName());
    }

    default <E extends Event> Consumer<FakeEventBus> hasDeliveredOnce(Class<E> eventType, Consumer<E> requirements) {

        return (FakeEventBus eventBus) -> {
            assertThat(eventBus).has(deliveredOnce(eventType));
            assertThat(eventBus.findFirstDelivered(eventType)).hasValueSatisfying(requirements);
        };
    }

    default <T> Matcher<FakeEventBus> deliveredEventOnce(Matcher<T> eventMatcher) {

        return new TypeSafeDiagnosingMatcher<>() {
            @Override
            protected boolean matchesSafely(FakeEventBus eventBus, Description mismatchDescription) {

                Long numMatches = eventBus.delivered().stream()
                                          .filter(eventMatcher::matches)
                                          .count();

                mismatchDescription.appendText("event bus delivered ")
                                   .appendValue(numMatches.intValue())
                                   .appendText(" times ");

                eventMatcher.describeTo(mismatchDescription);

                mismatchDescription.appendText("\nEvent bus was:\n")
                                   .appendValue(eventBus);

                return numMatches == 1;
            }

            @Override
            public void describeTo(Description description) {

                description.appendText("an event bus that has delivered exactly once ");
                eventMatcher.describeTo(description);
            }
        };

    }

    default Matcher<HavingDiagnosis> hasDiagnosedProblem(Problem expected) {

        return whereAttribute(x -> x.diagnosis().problem(),
                              "its diagnosed problem",
                              equalTo(expected)
        );
    }

    default <T> Matcher<ErrorOccurred> errorOccurredWithException(Class<T> type, Matcher<T> exceptionMatcher) {

        return instanceOf(ErrorOccurred.class,
                          whereAttribute(ErrorOccurred::asException, "its exception",
                                         instanceOf(type, exceptionMatcher)));
    }

}
