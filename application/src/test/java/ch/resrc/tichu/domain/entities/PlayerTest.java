package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
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
    assertThatNoException().isThrownBy(errorOrPlayer::get);
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
