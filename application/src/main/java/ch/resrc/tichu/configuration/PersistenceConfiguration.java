package ch.resrc.tichu.configuration;

import ch.resrc.tichu.adapters.persistence.game.MicroStreamGameRepository;
import ch.resrc.tichu.domain.repositories.GameRepository;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class PersistenceConfiguration {

  @ConfigProperty(name = "ch.resrc.tichu.data.games.storage-directory")
  Optional<String> storageDirectory;

  @ApplicationScoped
  public GameRepository gameRepository() {
    return new MicroStreamGameRepository(storageDirectory.orElse("data/games"));
  }
}
