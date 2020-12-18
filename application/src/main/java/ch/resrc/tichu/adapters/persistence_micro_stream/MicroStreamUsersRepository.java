package ch.resrc.tichu.adapters.persistence_micro_stream;

import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import one.microstream.afs.aws.s3.S3Connector;
import one.microstream.afs.blobstore.BlobStoreFileSystem;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import software.amazon.awssdk.services.s3.S3Client;

import javax.annotation.PreDestroy;

public class MicroStreamUsersRepository implements AddUser, GetAllUsers {

  private final UsersRoot usersRoot;
  private final EmbeddedStorageManager storage;

  public MicroStreamUsersRepository(S3Client client, String bucketName) {
    usersRoot = new UsersRoot();
    BlobStoreFileSystem fileSystem = BlobStoreFileSystem.New(S3Connector.Caching(client));
    storage = EmbeddedStorage.Foundation(fileSystem.ensureDirectoryPath(bucketName))
      .onConnectionFoundation(it -> it.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
      .start(usersRoot);
    storage.storeRoot();
  }

  @PreDestroy
  private void beforeDestroy() {
    storage.shutdown();
  }

  @Override
  public Either<? extends Problem, Set<User>> add(Set<User> existing, User toBeAdded) {
    return Either.right(usersRoot.update(existing.add(toBeAdded)));
  }

  @Override
  public Either<? extends Problem, Set<User>> getAll() {
    return Either.right(HashSet.ofAll(usersRoot.users()));
  }
}
