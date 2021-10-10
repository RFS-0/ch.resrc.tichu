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

class PlayerTest {

  @Test
  void legalValues_resultOf_validPlayer() {
    // given:
    Id id = Id.next();
    Name name = Name.resultOf("aPlayerName").get();
    Instant createdAt = Instant.now();

    // when:
    var errorOrPlayer = Player.create(id, name, createdAt);

    // then:
    errorOrPlayer.peekLeft(System.out::println);
    Player player = errorOrPlayer.get();
    assertThat(player.id()).isEqualTo(id);
    assertThat(player.name()).isEqualTo(name);
    assertThat(player.createdAt()).isEqualTo(createdAt);
  }

  @Test
  void nullId_resultOf_validationError() {
    // given:
    Id nullId = null;
    Name name = Name.resultOf("aPlayerName").get();
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(nullId);

    // when:
    var errorOrPlayer = Player.create(nullId, name, createdAt);

    // then:
    assertThatThrownBy(errorOrPlayer::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrPlayer.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullName_resultOf_validationError() {
    // given:
    Id id = Id.next();
    Name name = null;
    Instant createdAt = Instant.now();
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(name);

    // when:
    var errorOrPlayer = Player.create(id, name, createdAt);

    // then:
    assertThatThrownBy(errorOrPlayer::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrPlayer.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  void nullCreatedAt_resultOf_validationError() {
    // given:
    Id id = Id.next();
    Name name = Name.resultOf("aPlayerName").get();
    Instant nullCreatedAt = null;
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(nullCreatedAt);

    // when:
    var errorOrPlayer = Player.create(id, name, nullCreatedAt);

    // then:
    assertThatThrownBy(errorOrPlayer::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrPlayer.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }
}
