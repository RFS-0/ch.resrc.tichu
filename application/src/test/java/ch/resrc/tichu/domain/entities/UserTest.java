package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserTest {

  @Test
  @DisplayName("The [resultOf] legal values [Id, Email, Name, Name, Instant] is a valid user")
  void legalValues_resultOf_validTeam() {
    // given:
    Id id = Id.next();
    Email email = Email.resultOf("test@example.com").get();
    Name name = Name.resultOf("user name").get();
    Instant createdAt = Instant.now();

    // when:
    var errorOrUser = User.create(id, name, email, createdAt);

    // then:
    errorOrUser.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrUser::get);
    User user = errorOrUser.get();
    assertThat(user.id()).isEqualTo(id);
    assertThat(user.email()).isEqualTo(email);
    assertThat(user.name()).isEqualTo(name);
    assertThat(user.createdAt()).isEqualTo(createdAt);
  }

  @Test
  @DisplayName("The [resultOf] illegal values [null, Email, Name, Instant] is the validation error [MUST_NOT_BE_NULL]")
  void nullId_resultOf_expectedError() {
    // given:
    Id nullId = null;
    Email email = Email.resultOf("test@example.com").get();
    Name name = Name.resultOf("user name").get();
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = UserValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrUser = User.create(nullId, name, email, createdAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal values [Id, null, Name, Instant] is the validation error [MUST_NOT_BE_NULL]")
  void nullEmail_resultOf_expectedError() {
    // given:
    Id id = Id.next();
    Email nullEmail = null;
    Name name = Name.resultOf("user name").get();
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = UserValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrUser = User.create(id, name, nullEmail, createdAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal values [Id, Email, null, Instant] is the validation error [MUST_NOT_BE_NULL]")
  void nullName_resultOf_expectedError() {
    // given:
    Id id = Id.next();
    Email email = Email.resultOf("test@example.com").get();
    Name nullName = null;
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = UserValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrUser = User.create(id, nullName, email, createdAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal values [Id, Email, Name, null] is the validation error [MUST_NOT_BE_NULL]")
  void nullCreatedAt_resultOf_expectedError() {
    // given:
    Id id = Id.next();
    Email email = Email.resultOf("test@example.com").get();
    Name name = Name.resultOf("user name").get();
    Instant nullCreatedAt = null;
    ValidationError mustNotBeNullError = UserValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrUser = User.create(id, name, email, nullCreatedAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }
}
