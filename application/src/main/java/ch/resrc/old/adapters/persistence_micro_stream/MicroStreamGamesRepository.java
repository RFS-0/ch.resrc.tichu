package ch.resrc.old.adapters.persistence_micro_stream;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import io.vavr.collection.*;
import io.vavr.control.*;
import one.microstream.afs.aws.s3.*;
import one.microstream.afs.blobstore.*;
import one.microstream.reflect.*;
import one.microstream.storage.types.*;
import software.amazon.awssdk.services.s3.*;

import javax.annotation.*;

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
