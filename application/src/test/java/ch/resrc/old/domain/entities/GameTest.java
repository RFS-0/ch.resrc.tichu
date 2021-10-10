package ch.resrc.old.domain.entities;

import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;

import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class GameTest {

  @Test
  void legalValues_resultOf_validGame() {
    // given:
    Id gameId = Id.next();

    Id userId = Id.next();
    Email email = Email.resultOf("test@example.com").get();
    Name name = Name.resultOf("user name").get();
    Picture picture = Picture.resultOf("https://www.picture.com").get();
    Instant userCreatedAt = Instant.now();
    User createdBy = User.create(userId, name, email, userCreatedAt).get();

    JoinCode joinCode = JoinCode.next();

    Id leftTeamId = Id.next();
    Team leftTeam = Team.create(leftTeamId).get();
    Id rightTeamId = Id.next();
    Team rightTeam = Team.create(rightTeamId).get();
    Set<Team> teams = HashSet.of(leftTeam, rightTeam);

    Set<Round> rounds = HashSet.empty();

    Instant gameCreatedAt = Instant.now();

    // when:
    var errorOrGame = Game.create(gameId, createdBy, joinCode, teams, rounds, gameCreatedAt);

    // then:
    errorOrGame.peekLeft(System.out::println);
    Game game = errorOrGame.get();
    assertThat(game.id()).isEqualTo(gameId);
    assertThat(game.createdBy()).isEqualTo(createdBy);
    assertThat(game.joinCode()).isEqualTo(joinCode);
    assertThat(game.teams()).isEqualTo(teams);
    assertThat(game.rounds()).isEqualTo(rounds);
    assertThat(game.createdAt()).isEqualTo(gameCreatedAt);
    assertThat(game.finishedAt()).isNull();
  }
}
