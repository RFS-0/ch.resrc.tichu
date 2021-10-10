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
