package ch.resrc.old.use_cases.add_first_player_to_team;

import ch.resrc.old.adapters.persistence_in_memory.*;
import org.junit.jupiter.api.*;

class UpdateFirstPlayerOfTeamUseCaseTest {

  @Test
  void apply_teamHasNoFirstPlayer_expectedOutput() {
    // given
    InMemoryGamesRepository gamesRepository = new InMemoryGamesRepository();
    InMemoryTeamsRepository teamsRepository = new InMemoryTeamsRepository();
    InMemoryUsersRepository userRepository = new InMemoryUsersRepository();
    InMemoryPlayersRepository playersRepository = new InMemoryPlayersRepository();

    // when

    // then
  }
}
