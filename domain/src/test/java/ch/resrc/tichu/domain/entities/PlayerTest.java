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
        Id id = Id.next();
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
        Id missingId = null;
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
        Id id = Id.next();
        Name missingName = null;
        Instant createdAt = Instant.now();

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
        Id id = Id.next();
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
