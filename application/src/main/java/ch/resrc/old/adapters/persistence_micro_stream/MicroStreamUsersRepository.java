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
