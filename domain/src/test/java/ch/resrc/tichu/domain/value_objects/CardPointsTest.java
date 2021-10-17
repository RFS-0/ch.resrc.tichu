package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.entities.*;
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
        HashMap<TeamId, Integer> validInputValues = HashMap.of(
                TeamId.of("51ad87eb-9a10-4ba1-b979-217ee3522015"), -25,
                TeamId.of("773e80c6-e287-491b-ab0e-857c8c97ff52"), 125
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
        Map<TeamId, Integer> input = null;

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
        HashMap<TeamId, Integer> onlyOneTeam = HashMap.of(
                TeamId.of("508fe5e7-d6a3-4141-a4d9-b330782b0135"), 100
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
        HashMap<TeamId, Integer> valuesNotDivisibleBy5 = HashMap.of(
                TeamId.of("13ce997d-0285-4faa-978e-285ff919bf9a"), -24,
                TeamId.of("f29aa90f-11cc-4096-9c88-7f6806ee6efd"), 124
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
        HashMap<TeamId, Integer> invalidValues = HashMap.of(
                TeamId.of("3335350c-4b68-4ca6-a5b7-9ce6378aaafc"), tooSmallValue,
                TeamId.of("ac1366e9-4c16-4f83-882b-cdb36e4515ea"), 105
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
        HashMap<TeamId, Integer> invalidValues = HashMap.of(
                TeamId.of("9e74476b-be6f-4444-a5e5-9a0d860c52f0"), tooLargeValue,
                TeamId.of("5459dddf-44e7-48df-8691-1b9c2e03d1a6"), -25
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
        HashMap<TeamId, Integer> invalidValues = HashMap.of(
                TeamId.of("8e1a8aa3-5017-40be-82b1-da93224b4bc8"), 50,
                TeamId.of("57c6e46e-da7d-4729-9fa1-5a73181fb012"), 55
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
        HashMap<TeamId, Integer> invalidValues = HashMap.of(
                TeamId.of("77958085-1bf1-4513-a585-c8e09545c051"), tooLargeValueNotDivisibleBy5,
                TeamId.of("9f645ed2-0d2c-4708-b4e8-76ae066754ed"), tooSmallValue
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