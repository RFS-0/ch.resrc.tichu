package ch.resrc.old.domain.entities;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import org.assertj.core.api.*;
import org.junit.jupiter.api.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
