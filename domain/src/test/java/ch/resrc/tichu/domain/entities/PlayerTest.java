package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.api.*;

import java.time.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class PlayerTest {

    @Test
    void validValues_resultOf_success() {
        // given:
        var id = PlayerId.of("be259517-3d0c-44d8-a2ac-19de414207fb");
        Name name = Name.of("aPlayerName");
        Instant createdAt = Instant.now();

        // when:
        var result = Player.resultOf(id, name, createdAt);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void missingId_resultOf_failure() {
        // given:
        PlayerId missingId = null;
        Name name = Name.of("aPlayerName");
        Instant createdAt = Instant.now();

        // when:
        var result = Player.resultOf(missingId, name, createdAt);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(
                        whereErrorMessage(containsString("must not be null"))
                )
        );
    }

    @Test
    void missingName_resultOf_failure() {
        // given:
        var id = PlayerId.of("f2106d22-830b-48fc-af97-8073892a1cbe");
        Name missingName = null;
        var createdAt = Instant.now();

        // when:
        var result = Player.resultOf(id, missingName, createdAt);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(
                        whereErrorMessage(containsString("must not be null"))
                )
        );
    }

    @Test
    void missingCreatedAt_resultOf_failure() {
        // given:
        var id = PlayerId.of("eb6bd002-61f3-4c57-ba9a-52bcac87e223");
        Name name = Name.of("aPlayerName");
        Instant missingCreatedAt = null;

        // when:
        var result = Player.resultOf(id, name, missingCreatedAt);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(
                        whereErrorMessage(containsString("must not be null"))
                )
        );
    }
}
