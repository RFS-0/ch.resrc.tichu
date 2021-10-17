package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.test.capabilities.habits.fixtures.*;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;

import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class GameTest implements GameTestHabits {

    @Test
    void validValues_resultOf_success() {
        // given:
        var gameId = GameId.of("3280f70d-4ff9-4bed-9ef4-8c85e32da668");
        var createdBy = user(
                "4ac12a42-8581-4253-8902-418fd9b8cec5",
                "user name",
                "test@example.com",
                Instant.now()
        );
        var joinCode = joinCode("1484adb9");
        Set<Team> teams = HashSet.of(
                team("58b53592-0669-4e57-8b6f-8be5f91b6223"),
                team("a9b4237e-0ef3-4e4f-bfcc-b1219ad98365")
        );
        List<Round> rounds = List.empty();
        Instant gameCreatedAt = Instant.now();

        // when:
        var result = Game.resultOf(gameId, createdBy, joinCode, teams, rounds, gameCreatedAt);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
    }
}

interface GameTestHabits extends ValueObjectHabits, EntityHabits {

}
