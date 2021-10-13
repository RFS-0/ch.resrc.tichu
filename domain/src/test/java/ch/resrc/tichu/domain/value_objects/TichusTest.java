package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.hamcrest.Matchers.*;

class TichusTest {

    @Test
    void validValues_resultOf_success() {
        // given:
        Id firstPlayer = Id.next();
        Id secondPlayer = Id.next();
        Id thirdPlayer = Id.next();
        Id fourthPlayer = Id.next();
        HashMap<Id, Tichu> validValues = HashMap.of(
                firstPlayer, Tichu.NONE,
                secondPlayer, Tichu.NONE,
                thirdPlayer, Tichu.NONE,
                fourthPlayer, Tichu.TICHU_CALLED
        );

        // when:
        var result = Tichus.resultOf(validValues);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().tichuOfPlayer(firstPlayer)).isEqualTo(Tichu.NONE);
        assertThat(result.value().tichuOfPlayer(secondPlayer)).isEqualTo(Tichu.NONE);
        assertThat(result.value().tichuOfPlayer(thirdPlayer)).isEqualTo(Tichu.NONE);
        assertThat(result.value().tichuOfPlayer(fourthPlayer)).isEqualTo(Tichu.TICHU_CALLED);
    }

    @Test
    void validValuesForFivePlayers_resultOf_failure() {
        // given:
        Id firstPlayer = Id.next();
        Id secondPlayer = Id.next();
        Id thirdPlayer = Id.next();
        Id fourthPlayer = Id.next();
        Id fifthPlayer = Id.next();
        HashMap<Id, Tichu> validValues = HashMap.of(
                firstPlayer, Tichu.NONE,
                secondPlayer, Tichu.NONE,
                thirdPlayer, Tichu.NONE,
                fourthPlayer, Tichu.NONE,
                fifthPlayer, Tichu.TICHU_CALLED
        );

        // when:
        var result = Tichus.resultOf(validValues);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("exactly four players must have a tichu")))
        );
    }

    @Test
    void null_resultOf_expectedError() {
        // given:
        Map<Id, Tichu> invalidInput = null;

        // when:
        var result = Tichus.resultOf(invalidInput);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not be null")))
        );
    }

    @Test
    void nullAsTichuValue_resultOf_expectedError() {
        // given:
        Id firstPlayer = Id.next();
        Id secondPlayer = Id.next();
        Id thirdPlayer = Id.next();
        Id fourthPlayer = Id.next();
        HashMap<Id, Tichu> invalidTichuValue = HashMap.of(
                firstPlayer, Tichu.NONE,
                secondPlayer, Tichu.NONE,
                thirdPlayer, Tichu.NONE,
                fourthPlayer, null
        );

        // when:
        var result = Tichus.resultOf(invalidTichuValue);

        // then:
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not contain null")))
        );
    }

    @Test
    void nullKey_resultOf_expectedError() {
        // given:
        Id secondPlayer = Id.next();
        Id thirdPlayer = Id.next();
        Id fourthPlayer = Id.next();
        Tichu nullValue = null;
        HashMap<Id, Tichu> invalidPlayerId = HashMap.of(
                null, Tichu.NONE,
                secondPlayer, Tichu.NONE,
                thirdPlayer, Tichu.NONE,
                fourthPlayer, nullValue
        );

        // when:
        var result = Tichus.resultOf(invalidPlayerId);

        // then:
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not contain null")))
        );
    }
}
