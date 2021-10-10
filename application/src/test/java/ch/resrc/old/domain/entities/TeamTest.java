package ch.resrc.old.domain.entities;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import org.assertj.core.api.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class TeamTest {

  @Test
  void legalValues_resultOf_validTeam() {
    // given:
    Id id = Id.next();

    // when:
    var errorOrTeam = Team.create(id);

    // then:
    errorOrTeam.peekLeft(System.out::println);
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
