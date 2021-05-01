package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.UpdateTeam;
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

public class MicroStreamTeamsRepository implements AddTeam, GetAllTeams, UpdateTeam {

  private final TeamsRoot teamsRoot;
  private final EmbeddedStorageManager storage;

  public MicroStreamTeamsRepository(S3Client client, String bucketName) {
    this.teamsRoot = new TeamsRoot();
    BlobStoreFileSystem fileSystem = BlobStoreFileSystem.New(S3Connector.Caching(client));
    storage = EmbeddedStorage.Foundation(fileSystem.ensureDirectoryPath(bucketName))
      .onConnectionFoundation(it -> it.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
      .start(teamsRoot);
    storage.storeRoot();
  }

  @PreDestroy
  private void beforeDestroy() {
    storage.shutdown();
  }

  @Override
  public Either<? extends Problem, Set<Team>> add(Set<Team> existing, Team toBeAdded) {
    return Either.right(teamsRoot.update(existing.add(toBeAdded)).peek(storage::store));
  }

  @Override
  public Either<? extends Problem, Set<Team>> getAll() {
    return Either.right(HashSet.ofAll(teamsRoot.teams()));
  }

  @Override
  public Either<? extends Problem, Set<Team>> update(Set<Team> existing, Team updatedTeam) {
    Option<Team> maybeToBeUpdated = existing.find(team -> team.id().equals(updatedTeam.id()));
    if (!maybeToBeUpdated.isDefined()) {
      return Either.left(PersistenceProblem.UPDATE_FAILED_DUE_TO_MISSING_ENTITY);
    }

    Team teamToUpdate = maybeToBeUpdated.get();

    teamsRoot.update(existing.remove(teamToUpdate).add(updatedTeam));

    return Either.right(teamsRoot.teams());
  }
}
