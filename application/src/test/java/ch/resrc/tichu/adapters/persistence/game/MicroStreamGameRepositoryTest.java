package ch.resrc.tichu.adapters.persistence.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.repositories.GameRepository;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.JoinCode;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MicroStreamGameRepositoryTest {

  private final static String STORAGE_DIRECTORY = System.getProperty("user.dir") + "/test/data/games";

  @Test
  void shutdown_repoCanBeShutDown() {
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var shutdownSuccessful = repository.shutdown();
    assertThat(shutdownSuccessful).isTrue();
  }

  @Test
  void insert_aGame_theGameCanBeFoundAfterRestart() {
    // given
    final var aGame = validGame();
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    assertDoesNotThrow(repository::deleteAll);

    // when
    repository.insert(aGame);
    repository.shutdown();

    // then
    final var restartedRepository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var found = restartedRepository.findById(aGame.id());
    assertThat(found).isPresent();
    final var aPersistedGame = found.get();
    assertThat(aGame).isEqualTo(aPersistedGame);

    // finally
    cleanUp(restartedRepository);
  }

  @Test
  void insertAll_aGame_anotherGame_theGamesCanBeFoundAfterRestart() {
    // given
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var aGame = validGame();
    final var anotherGame = validGame();

    // when
    repository.insertAll(Set.of(aGame, anotherGame));
    repository.shutdown();

    // then
    final var restartedRepository = new MicroStreamGameRepository(STORAGE_DIRECTORY);

    final var aPersistedGame = restartedRepository.findById(aGame.id()).orElse(null);
    assertThat(aGame).isEqualTo(aPersistedGame);

    final var anotherPersistedGame = restartedRepository.findById(anotherGame.id()).orElse(null);
    assertThat(anotherGame).isEqualTo(anotherPersistedGame);

    assertThat(restartedRepository.all().size()).isEqualTo(2);

    // finally
    cleanUp(restartedRepository);
  }

  @Test
  void delete_existingGames_deletedGameCanNotBeFound() {
    // given
    final var aGame = validGame();
    final var anotherGame = validGame();
    insertGames(aGame, anotherGame);

    // when
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    assertThat(repository.delete(aGame)).isTrue();
    repository.shutdown();

    // then
    final var restartedRepository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    assertThat(restartedRepository.all().size()).isEqualTo(1);

    // finally
    cleanUp(restartedRepository);
  }

  @Test
  void deleteAll_existingGames_noGamesFound() {
    // given
    final var aGame = validGame();
    final var anotherGame = validGame();
    insertGames(aGame, anotherGame);

    // when
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    repository.deleteAll();
    repository.shutdown();

    // then
    final var restartedRepository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    assertThat(restartedRepository.all().isEmpty()).isTrue();

    // finally
    cleanUp(restartedRepository);
  }

  @Test
  void all_withMultipleGames_allGamesFound() {
    // given
    final var aGame = validGame();
    final var anotherGame = validGame();
    insertGames(aGame, anotherGame);

    // when
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var found = repository.all();

    // then
    assertThat(found.size()).isEqualTo(2);
    final var aPersistedGame = repository.findById(aGame.id()).orElse(null);
    assertThat(aGame).isEqualTo(aPersistedGame);

    // finally
    cleanUp(repository);
  }

  @Test
  void findById_withMultipleGames_searchedGameFound() {
    // given
    final var aGame = validGame();
    final var anotherGame = validGame();
    insertGames(aGame, anotherGame);

    // when
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var found = repository.findById(aGame.id());

    // then
    assertThat(found).isPresent();
    assertThat(found.get()).isEqualTo(aGame);

    // finally
    cleanUp(repository);
  }

  @Test
  void update() {
    // given
    final var aGame = validGame();
    insertGames(aGame);

    // when
    final var initialRepository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var newJoinCode = JoinCode.next();
    final var persisted = initialRepository.findById(aGame.id()).orElse(null);
    assertThat(persisted).isNotNull();
    final var updated = persisted.finishedAt(Instant.now()).value();
    initialRepository.update(updated);
    initialRepository.shutdown();

    // then
    final var updatedRepository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    final var found = updatedRepository.findById(aGame.id()).orElse(null);
    assertThat(found).isEqualTo(updated);

    // finally
    cleanUp(updatedRepository);
  }

  private Game validGame() {
    return Game.createdBy(Id.next()).value();
  }

  private void insertGames(Game... games) {
    final var repository = new MicroStreamGameRepository(STORAGE_DIRECTORY);
    repository.deleteAll();
    repository.insertAll(List.of(games));
    repository.shutdown();
  }

  private void cleanUp(GameRepository repository) {
    repository.deleteAll();
    repository.shutdown();
  }
}
