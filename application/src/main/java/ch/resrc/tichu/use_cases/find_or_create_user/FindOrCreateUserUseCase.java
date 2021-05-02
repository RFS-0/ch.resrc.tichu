package ch.resrc.tichu.use_cases.find_or_create_user;

import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDetected;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
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

import static ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected.supplierFor;

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
