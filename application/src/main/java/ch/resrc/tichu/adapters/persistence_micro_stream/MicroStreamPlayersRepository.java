package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.UpdatePlayer;
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

public class MicroStreamPlayersRepository implements AddPlayer, GetAllPlayers, UpdatePlayer {

  private final PlayersRoot playersRoot;
  private final EmbeddedStorageManager storage;

  public MicroStreamPlayersRepository(S3Client client, String bucketName) {
    this.playersRoot = new PlayersRoot();
    BlobStoreFileSystem fileSystem = BlobStoreFileSystem.New(S3Connector.Caching(client));
    storage = EmbeddedStorage.Foundation(fileSystem.ensureDirectoryPath(bucketName))
      .onConnectionFoundation(it -> it.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
      .start(playersRoot);
    storage.storeRoot();
  }

  @PreDestroy
  private void beforeDestroy() {
    storage.shutdown();
  }

  @Override
  public Either<? extends Problem, Set<Player>> add(Set<Player> existing, Player toBeAdded) {
    return Either.right(playersRoot.update(existing.add(toBeAdded)).peek(storage::store));
  }

  @Override
  public Either<? extends Problem, Set<Player>> getAll() {
    return Either.right(HashSet.ofAll(playersRoot.players()));
  }

  @Override
  public Either<? extends Problem, Set<Player>> update(Set<Player> existing, Player updatedPlayer) {
    Option<Player> maybeToBeUpdated = existing.find(team -> team.id().equals(updatedPlayer.id()));
    if (!maybeToBeUpdated.isDefined()) {
      return Either.left(PersistenceProblem.UPDATE_FAILED_DUE_TO_MISSING_ENTITY);
    }

    Player teamToUpdate = maybeToBeUpdated.get();

    playersRoot.update(existing.remove(teamToUpdate).add(updatedPlayer));

    return Either.right(playersRoot.players());
  }
}
