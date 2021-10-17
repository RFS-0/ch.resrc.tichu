package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import org.junit.jupiter.api.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class TeamTest {

  @Test
  void legalValues_resultOf_validTeam() {
    // given:
    var id = TeamId.of("d2a80876-4f06-449f-99eb-45949bbe2ead");

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
    TeamId missingId = null;

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
