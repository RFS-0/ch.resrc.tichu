package ch.resrc.tichu.use_cases.add_first_player_to_team;

import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryGamesRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryPlayersRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryTeamsRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryUsersRepository;
import org.junit.jupiter.api.Test;

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
