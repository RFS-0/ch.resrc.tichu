package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.entities.*;
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
        var firstPlayer = PlayerId.of("38aabc86-33cd-46bb-a705-80c4de7239fa");
        var secondPlayer = PlayerId.of("0f247cd1-cc11-40be-b0f8-8032f7d6ca02");
        var thirdPlayer = PlayerId.of("c8cefa3a-3e92-4020-816c-0647ed82c1bc");
        var fourthPlayer = PlayerId.of("1dad6c29-6a85-4121-824a-82227a4e70e0");
        HashMap<PlayerId, Tichu> validValues = HashMap.of(
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
        HashMap<PlayerId, Tichu> validValues = HashMap.of(
                PlayerId.of("1ae7de97-fd73-4bd7-a7a6-bb059eb07a83"), Tichu.NONE,
                PlayerId.of("81234114-7153-4f67-9ab0-1dd6d31b9e7a"), Tichu.NONE,
                PlayerId.of("4e60c855-43a5-4bf7-8e56-1ed14d9716af"), Tichu.NONE,
                PlayerId.of("4daaa845-2b95-4a43-9262-90edc14953d4"), Tichu.NONE,
                PlayerId.of("7c4a4b1d-589a-47a5-ae3f-cb3101bec622"), Tichu.TICHU_CALLED
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
        Map<PlayerId, Tichu> invalidInput = null;

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
        HashMap<PlayerId, Tichu> invalidTichuValue = HashMap.of(
                PlayerId.of("045003f9-d948-4002-9c35-d5a37dc01c8f"), Tichu.NONE,
                PlayerId.of("643b4c3a-0b84-48d2-a710-eda340db8b11"), Tichu.NONE,
                PlayerId.of("9df63e0f-e4bf-43d7-ad40-6880c8d4f258"), Tichu.NONE,
                PlayerId.of("f4573ecc-693a-4c37-861c-daecedc67f9e"), null
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
        Tichu nullValue = null;
        HashMap<PlayerId, Tichu> invalidPlayerId = HashMap.of(
                null, Tichu.NONE,
                PlayerId.of("f0ef4ec1-fc6a-42a5-847f-5d638bddb15b"), Tichu.NONE,
                PlayerId.of("5dae41ff-2258-4c40-91d2-7b0bc0911aaf"), Tichu.NONE,
                PlayerId.of("33bca59a-a5b7-4d5a-bb39-ec4831d6c4d7"), nullValue
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
