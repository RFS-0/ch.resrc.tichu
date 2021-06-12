package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.validation.DomainValidationErrors;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.collection.Seq;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TeamTest {

  @Test
  void legalValues_resultOf_validTeam() {
    // given:
    Id id = Id.next();

    // when:
    var errorOrTeam = Team.create(id);

    // then:
    errorOrTeam.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrTeam::get);
    Team team = errorOrTeam.get();
    assertThat(team.id()).isEqualTo(id);
  }

  @ParameterizedTest
  @NullSource
  void nullId_resultOf_validationError(Id illegalId) {
    // given:
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(illegalId);

    // when:
    var errorOrTeam = Team.create(illegalId);

    // then:
    assertThatThrownBy(errorOrTeam::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrTeam.getLeft();
    AssertionsForInterfaceTypes.assertThat(errors).hasSize(1);
    AssertionsForInterfaceTypes.assertThat(errors).contains(mustNotBeNullError);
  }
}
