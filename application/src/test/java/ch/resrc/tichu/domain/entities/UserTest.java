package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserTest {

  @Test
  void legalValues_resultOf_validUser() {
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
  void nullId_resultOf_validationError() {
    // given:
    Id nullId = null;
    Email email = Email.resultOf("test@example.com").get();
    Name name = Name.resultOf("user name").get();
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(nullId);

    // when:
    var errorOrUser = User.create(nullId, name, email, createdAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullEmail_resultOf_validationError() {
    // given:
    Id id = Id.next();
    Email nullEmail = null;
    Name name = Name.resultOf("user name").get();
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(nullEmail);

    // when:
    var errorOrUser = User.create(id, name, nullEmail, createdAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullName_resultOf_validationError() {
    // given:
    Id id = Id.next();
    Email email = Email.resultOf("test@example.com").get();
    Name nullName = null;
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(nullName);

    // when:
    var errorOrUser = User.create(id, nullName, email, createdAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullCreatedAt_resultOf_validationError() {
    // given:
    Id id = Id.next();
    Email email = Email.resultOf("test@example.com").get();
    Name name = Name.resultOf("user name").get();
    Instant nullCreatedAt = null;
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(nullCreatedAt);

    // when:
    var errorOrUser = User.create(id, name, email, nullCreatedAt);

    // then:
    assertThatThrownBy(errorOrUser::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrUser.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }
}
