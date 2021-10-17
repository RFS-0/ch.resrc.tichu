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
        var id = UserId.of("ecc32918-5924-4a4c-9120-8cf5dbe994c6");
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
        UserId missingId = null;
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
        var id = UserId.of("6bc4a7e3-dbb4-491c-a62e-96f7c7c40836");
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
        var id = UserId.of("4fc10782-218c-4212-af8c-441d42ff3f05");
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
        var id = UserId.of("5770cf67-4e07-4581-8a3b-4aa191de4984");
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
