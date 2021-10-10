package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class CardPointsTest {

    @Test
    void validValues_resultOf_success() {
        // given:
        HashMap<Id, Integer> validInputValues = HashMap.of(
                Id.next(), -25,
                Id.next(), 125
        );

        // when:
        var result = CardPoints.resultOf(validInputValues);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void null_resultOf_failure() {
        // given:
        Map<Id, Integer> input = null;

        // when:
        var result = CardPoints.resultOf(input);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("cannot be undefined")))
        );
    }

    @Test
    void onlyOneTeamHasCardPoints_resultOf_failure() {
        // given:
        HashMap<Id, Integer> onlyOneTeam = HashMap.of(
                Id.next(), 100
        );

        // when:
        var result = CardPoints.resultOf(onlyOneTeam);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must be equal to <2>")))
        );
    }

    @Test
    void valuesNotDivisibleBy5_resultOf_failure() {
        // given:
        HashMap<Id, Integer> valuesNotDivisibleBy5 = HashMap.of(
                Id.next(), -24,
                Id.next(), 124
        );

        // when:
        var result = CardPoints.resultOf(valuesNotDivisibleBy5);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("all values must be divisible by <5>")))
        );
    }

    @Test
    void tooSmallValue_resultOf_failure() {
        // given:
        var tooSmallValue = -30;
        HashMap<Id, Integer> invalidValues = HashMap.of(
                Id.next(), tooSmallValue,
                Id.next(), 105
        );

        // when:
        var result = CardPoints.resultOf(invalidValues);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("all values must be greater or equal to <-25>")))
        );
    }

    @Test
    void tooLargeValue_resultOf_failure() {
        // given:
        var tooLargeValue = 130;
        HashMap<Id, Integer> invalidValues = HashMap.of(
                Id.next(), tooLargeValue,
                Id.next(), -25
        );

        // when:
        var result = CardPoints.resultOf(invalidValues);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("all values must be smaller or equal to <125>")))
        );
    }

    @Test
    void totalNotEqualTo100_resultOf_failure() {
        // given:
        HashMap<Id, Integer> invalidValues = HashMap.of(
                Id.next(), 50,
                Id.next(), 55
        );

        // when:
        var result = CardPoints.resultOf(invalidValues);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("must be equal to <100>")))
        );
    }

    @Test
    void tooLargeValueNotDivisibleBy5AndTooSmallValue_resultOf_failure() {
        // given:
        var tooLargeValueNotDivisibleBy5 = 141;
        var tooSmallValue = -30;
        HashMap<Id, Integer> invalidValues = HashMap.of(
                Id.next(), tooLargeValueNotDivisibleBy5,
                Id.next(), tooSmallValue
        );

        // when:
        var result = CardPoints.resultOf(invalidValues);

        // then:
        assertThat(result.isFailure()).isTrue();
        assertThat(result.errors().size()).isEqualTo(4);
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("all values must be smaller or equal to <125>")))
        );
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("all values must be greater or equal to <-25>")))
        );
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("all values must be divisible by <5>")))
        );
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("must be equal to <100>")))
        );
    }
}