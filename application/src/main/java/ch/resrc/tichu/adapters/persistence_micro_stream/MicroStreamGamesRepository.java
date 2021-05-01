package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.UpdateGame;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import io.vavr.control.Option;
import one.microstream.afs.aws.s3.S3Connector;
import one.microstream.afs.blobstore.BlobStoreFileSystem;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import software.amazon.awssdk.services.s3.S3Client;

import javax.annotation.PreDestroy;

public class MicroStreamGamesRepository implements AddGame, GetAllGames, UpdateGame {

  private final GamesRoot gamesRoot;
  private final EmbeddedStorageManager storage;

  public MicroStreamGamesRepository(S3Client client, String bucketName) {
    gamesRoot = new GamesRoot();
    BlobStoreFileSystem fileSystem = BlobStoreFileSystem.New(S3Connector.Caching(client));
    storage = EmbeddedStorage.Foundation(fileSystem.ensureDirectoryPath(bucketName))
      .onConnectionFoundation(it -> it.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
      .start(gamesRoot);
    storage.storeRoot();
  }

  @PreDestroy
  private void beforeDestroy() {
    storage.shutdown();
  }

  @Override
  public Either<PersistenceProblem, Set<Game>> add(Set<Game> existing, Game toBeAdded) {
    return Either.right(gamesRoot.update(existing.add(toBeAdded)).peek(storage::store));
  }

  @Override
  public Either<? extends Problem, Set<Game>> getAll() {
    return Either.right(HashSet.ofAll(gamesRoot.games()));
  }

  @Override
  public Either<? extends Problem, Set<Game>> update(Set<Game> existing, Game updatedGame) {
    Option<Game> maybeToBeUpdated = existing.find(game -> game.id().equals(updatedGame.id()));
    if (!maybeToBeUpdated.isDefined()) {
      return Either.left(PersistenceProblem.UPDATE_FAILED_DUE_TO_MISSING_ENTITY);
    }

    Game teamToUpdate = maybeToBeUpdated.get();

    gamesRoot.update(existing.remove(teamToUpdate).add(updatedGame));

    return Either.right(gamesRoot.games());
  }
}
