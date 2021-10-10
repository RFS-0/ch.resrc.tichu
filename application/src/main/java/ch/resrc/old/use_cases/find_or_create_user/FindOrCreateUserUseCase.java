package ch.resrc.old.use_cases.find_or_create_user;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.input.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.time.*;

import static ch.resrc.old.capabilities.errorhandling.DomainProblemDetected.*;

public class FindOrCreateUserUseCase implements FindOrCreateUserInput {

  private final GetAllUsers getAllUsers;
  private final AddUser addUser;
  private final GetAllPlayers getAllPlayers;
  private final AddPlayer addPlayer;

  public FindOrCreateUserUseCase(GetAllUsers getAllUsers, AddUser addUser, GetAllPlayers getAllPlayers, AddPlayer addPlayer) {
    this.getAllUsers = getAllUsers;
    this.addUser = addUser;
    this.getAllPlayers = getAllPlayers;
    this.addPlayer = addPlayer;
  }

  @Override
  public FindOrCreateUserOutput.Response apply(Request requested) {
    IntendedUser intent = requested.intent();

    Set<User> existingUsers = getAllUsers.getAll().getOrElseThrow(supplierFor(PersistenceProblem.READ_FAILED));
    Set<Player> existingPlayers = getAllPlayers.getAll().getOrElseThrow(supplierFor(PersistenceProblem.READ_FAILED));

    Option<User> existingUser = existingUsers.find(
      user -> user.name().equals(intent.name()) && user.email().equals(intent.email())
    );
    if (existingUser.isDefined()) {
      return new FindOrCreateUserOutput.Response(
        UserDocument.fromUser(existingUser.get())
      );
    }

    Id userId = Id.next();
    Instant now = Instant.now();
    User user = User.create(userId, intent.name(), intent.email(), now).get();
    Player player = Player.create(userId, intent.name(), now).get();

    Either<? extends Problem, Set<User>> userAddedOrError = addUser.add(existingUsers, user);
    if (userAddedOrError.isLeft()) {
      ProblemDetected.throwProblem(PersistenceProblem.INSERT_FAILED, userAddedOrError.getLeft());
    }

    Either<? extends Problem, Set<Player>> playerAddedOrError = addPlayer.add(existingPlayers, player);
    if (playerAddedOrError.isLeft()) {
      ProblemDetected.throwProblem(PersistenceProblem.INSERT_FAILED, playerAddedOrError.getLeft());
    }

    return new FindOrCreateUserOutput.Response(
      UserDocument.fromUser(user)
    );
  }
}
