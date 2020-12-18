package ch.resrc.tichu.use_cases.find_or_create_user;

import ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDetected;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.FindOrCreateUserInput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.IntendedUser;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.FindOrCreateUserOutput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.UserDocument;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.time.Instant;

public class FindOrCreateUserUseCase implements FindOrCreateUserInput {

  private final AddUser addUser;
  private final GetAllUsers getAllUsers;

  public FindOrCreateUserUseCase(AddUser addUser, GetAllUsers getAllUsers) {
    this.addUser = addUser;
    this.getAllUsers = getAllUsers;
  }

  @Override
  public FindOrCreateUserOutput.Response apply(Request requested) {
    IntendedUser intent = requested.intent();

    Set<User> existingUsers = getAllUsers.getAll().getOrElseThrow(
      () -> DomainProblemDetected.of(PersistenceProblem.READ_FAILED)
    );

    Option<User> existingUser = existingUsers.find(
      user -> user.name().equals(intent.name()) && user.email().equals(intent.email())
    );
    if (existingUser.isDefined()) {
      return new FindOrCreateUserOutput.Response(
        UserDocument.fromUser(existingUser.get())
      );
    }

    User user = User.create(Id.next(), intent.name(), intent.email(), Instant.now()).get();

    Either<? extends Problem, Set<User>> addedOrError = addUser.add(existingUsers, user);
    if (addedOrError.isLeft()) {
      throw ProblemDetected.of(
        ProblemDiagnosis.of(PersistenceProblem.INSERT_FAILED)
          .withContext("message", addedOrError.getLeft())
      );
    }

    return new FindOrCreateUserOutput.Response(
      UserDocument.fromUser(user)
    );
  }
}
