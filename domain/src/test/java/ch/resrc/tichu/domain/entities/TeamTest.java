package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.api.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class TeamTest {

  @Test
  void legalValues_resultOf_validTeam() {
    // given:
    Id id = Id.next();

    // when:
    var result = Team.resultOf(id);

    // then:
    result.failureEffect(System.out::println);
    Team team = result.value();
    assertThat(team.id()).isEqualTo(id);
  }

  @Test
  void missingId_resultOf_validationError() {
    // given:
    Id missingId = null;

    // when:
    var result = Team.resultOf(missingId);

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
