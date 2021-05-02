package ch.resrc.tichu.use_cases.create_a_game;

import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryGamesRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryPlayersRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryTeamsRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryUsersRepository;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.common_ports.output.PlayerDocument;
import ch.resrc.tichu.use_cases.common_ports.output.TeamDocument;
import ch.resrc.tichu.use_cases.create_a_game.ports.input.CreateGameInput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.CreateGameOutput;
import ch.resrc.tichu.use_cases.create_a_game.ports.output.GameDocument;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.UserDocument;
import io.vavr.collection.HashSet;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Objects;

import static ch.resrc.tichu.use_cases.create_a_game.ports.input.IntendedGame.anIntendedGame;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateGameUseCaseTest {

  @Test
  void apply_playerDoesNotExistYet_expectedOutput() {
    // given
    Id createdByUser = Id.next();
    Name stickyMickey = Name.resultOf("Sticky Mickey").get();
    Email email = Email.resultOf("sticky@mickey.com").get();
    Instant createdAt = Instant.now();
    User existingUser = User.create(createdByUser, stickyMickey, email, createdAt).get();
    CreateGameInput.Request request = createGameRequest(createdByUser);

    InMemoryGamesRepository gamesRepository = new InMemoryGamesRepository();
    InMemoryTeamsRepository teamsRepository = new InMemoryTeamsRepository();
    InMemoryUsersRepository userRepository = new InMemoryUsersRepository();
    userRepository.add(HashSet.empty(), existingUser);
    InMemoryPlayersRepository playersRepository = new InMemoryPlayersRepository();

    CreateGameUseCase createGameUseCase = new CreateGameUseCase(
      gamesRepository,
      gamesRepository,
      teamsRepository,
      teamsRepository,
      playersRepository,
      playersRepository,
      userRepository
    );

    // when
    CreateGameOutput.Response useCaseResponse = createGameUseCase.apply(request);

    // then
    assertNotNull(useCaseResponse);
    GameDocument gameDocument = useCaseResponse.toBePresented();
    assertNotNull(gameDocument.id());
    assertThat(gameDocument.createdBy()).isEqualTo(UserDocument.fromUser(existingUser));
    assertThat(gameDocument.teams()).hasSize(2);
    assertThat(
      gameDocument.teams()
        .filter(Objects::nonNull)
        .map(TeamDocument::firstPlayer)
        .filter(Objects::nonNull)
        .map(PlayerDocument::id)
        .filter(Objects::nonNull)
    )
      .contains(
        createdByUser
      );
    assertThat(playersRepository.getAll().get()).hasSize(1);
  }

  private CreateGameInput.Request createGameRequest(Id createdBy) {
    return new CreateGameInput.Request(
      anIntendedGame()
        .withCreatedBy(createdBy)
        .buildResult().get()
    );
  }
}
