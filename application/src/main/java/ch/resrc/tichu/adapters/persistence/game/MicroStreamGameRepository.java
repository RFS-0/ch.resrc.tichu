package ch.resrc.tichu.adapters.persistence.game;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.repositories.GameRepository;
import ch.resrc.tichu.domain.value_objects.Id;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PreDestroy;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.jboss.logging.Logger;

public class MicroStreamGameRepository implements GameRepository {

  private static final Logger LOG = Logger.getLogger(MicroStreamGameRepository.class);

  private final GameRoot gameRoot;
  private final EmbeddedStorageManager storage;

  public MicroStreamGameRepository(String storageDirectory) {
    gameRoot = new GameRoot();
    storage = Configuration.Default()
      .setBaseDirectory(storageDirectory)
      .setChannelCount(4)
      .createEmbeddedStorageFoundation()
      .createEmbeddedStorageManager(gameRoot)
      .start();
  }

  @PreDestroy
  private void beforeDestroy() {
    shutdown();
  }

  @Override
  public void insert(Game game) {
    gameRoot.games().add(game);
    storage.store(gameRoot.games());
  }

  @Override
  public void insertAll(Collection<Game> games) {
    this.gameRoot.games().addAll(games);
    storage.store(this.gameRoot.games());
  }

  @Override
  public boolean delete(Game game) {
    final var successful = gameRoot.games().remove(game);
    storage.store(gameRoot.games());
    return successful;
  }

  @Override
  public void deleteAll() {
    gameRoot.games().clear();
    storage.store(gameRoot.games());
  }

  @Override
  public boolean shutdown() {
    final var successful = storage.shutdown();
    if (successful) {
      LOG.info("Shutdown of " + MicroStreamGameRepository.class.getSimpleName() + " was successful.");
    } else {
      LOG.error("Shutdown of " + MicroStreamGameRepository.class.getSimpleName() + " was failed.");
    }
    return successful;
  }

  @Override
  public Set<Game> all() {
    return gameRoot.games();
  }

  @Override
  public Optional<Game> findById(Id gameId) {
    return gameRoot.games().stream()
      .filter(game -> game.id().equals(gameId))
      .findFirst();
  }

  @Override
  public void update(Game game) {
    storage.store(game);
  }
}
