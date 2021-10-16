package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.api.*;

import java.time.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class UserTest {

    @Test
    void validValues_resultOf_success() {
        // given:
        Id id = Id.next();
        Email email = Email.of("test@example.com");
        Name name = Name.of("user name");
        Instant createdAt = Instant.now();

        // when:
        var result = User.resultOf(id, name, email, createdAt);

        // then:
        result.failureEffect(System.out::println);
        User user = result.value();
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.email()).isEqualTo(email);
        assertThat(user.name()).isEqualTo(name);
        assertThat(user.createdAt()).isEqualTo(createdAt);
    }

    @Test
    void missingId_resultOf_validationError() {
        // given:
        Id missingId = null;
        Email email = Email.of("test@example.com");
        Name name = Name.of("user name");
        Instant createdAt = Instant.now();

        // when:
        var result = User.resultOf(missingId, name, email, createdAt);

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
    void missingEmail_resultOf_validationError() {
        // given:
        Id id = Id.next();
        Email missingEmail = null;
        Name name = Name.of("user name");
        Instant createdAt = Instant.now();

        // when:
        var result = User.resultOf(id, name, missingEmail, createdAt);

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
    void missingName_resultOf_validationError() {
        // given:
        Id id = Id.next();
        Email email = Email.of("test@example.com");
        Name missingName = null;
        Instant createdAt = Instant.now();

        // when:
        var result = User.resultOf(id, missingName, email, createdAt);

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
    void missingCreatedAt_resultOf_validationError() {
        // given:
        Id id = Id.next();
        Email email = Email.of("test@example.com");
        Name name = Name.of("user name");
        Instant missingCreatedAt = null;

        // when:
        var result = User.resultOf(id, name, email, missingCreatedAt);

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
