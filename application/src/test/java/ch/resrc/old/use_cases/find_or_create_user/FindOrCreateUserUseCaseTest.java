package ch.resrc.old.use_cases.find_or_create_user;

import ch.resrc.old.adapters.persistence_in_memory.*;
import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.input.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;
import io.vavr.collection.*;
import io.vavr.control.*;
import org.junit.jupiter.api.*;

import java.time.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FindOrCreateUserUseCaseTest {

  @Test
  void apply_findOrCreateUserRequestForNewUser_expectedOutput() {
    // given
    Name archibaldCobb = Name.resultOf("Archibald Cobb").get();
    Email email = Email.resultOf("archibald@cobb.com").get();
    var createUserRequest = createFindOrCreateUserRequest(archibaldCobb, email);

    InMemoryUsersRepository fakeUsersRepository = new InMemoryUsersRepository();
    InMemoryPlayersRepository fakePlayersRepository = new InMemoryPlayersRepository();

    FindOrCreateUserUseCase findOrCreateUserUseCase = new FindOrCreateUserUseCase(
      fakeUsersRepository,
      fakeUsersRepository,
      fakePlayersRepository,
      fakePlayersRepository
    );

    // when
    FindOrCreateUserOutput.Response createUserResponse = findOrCreateUserUseCase.apply(createUserRequest);

    // then
    UserDocument userDocument = createUserResponse.toBePresented();
    assertThat(userDocument).isNotNull();
    assertThat(userDocument.name()).isEqualTo(archibaldCobb);
    assertThat(userDocument.email()).isEqualTo(email);
    assertThat(userDocument.id()).isNotNull();
    assertThat(userDocument.createdAt()).isBefore(Instant.now());
  }

  @Test
  void apply_findOrCreateUserRequestForExistingUser_expectedFindOrCreateUserRequest() {
    // given
    Id id = Id.next();
    Name archibaldCobb = Name.resultOf("Archibald Cobb").get();
    Email email = Email.resultOf("archibald@cobb.com").get();
    Instant now = Instant.now();
    User existingUser = User.create(id, archibaldCobb, email, now).get();
    var createUserRequest = createFindOrCreateUserRequest(archibaldCobb, email);

    InMemoryUsersRepository fakeUserRepository = new InMemoryUsersRepository();
    InMemoryPlayersRepository fakePlayersRepository = new InMemoryPlayersRepository();
    fakeUserRepository.add(HashSet.empty(), existingUser);

    FindOrCreateUserUseCase findOrCreateUserUseCase = new FindOrCreateUserUseCase(
      fakeUserRepository,
      fakeUserRepository,
      fakePlayersRepository,
      fakePlayersRepository
    );

    // when
    var createUserResponse = findOrCreateUserUseCase.apply(createUserRequest);

    // then
    UserDocument userDocument = createUserResponse.toBePresented();
    assertThat(userDocument).isNotNull();
    assertThat(userDocument.id()).isEqualTo(id);
    assertThat(userDocument.name()).isEqualTo(archibaldCobb);
    assertThat(userDocument.email()).isEqualTo(email);
    assertThat(userDocument.createdAt()).isEqualTo(now);
  }

  @Test
  void apply_getAllUsersFails_persistenceProblem() {
    Name nateSweeney = Name.resultOf("Nate Sweeney").get();
    Email email = Email.resultOf("nate@sweeney.org").get();
    var findOrCreateUserRequest = createFindOrCreateUserRequest(nateSweeney, email);

    InMemoryUsersRepository fakeUserRepository = new InMemoryUsersRepository();
    GetAllUsers getAllUsersFailure = () -> Either.left(PersistenceProblem.READ_FAILED);
    InMemoryPlayersRepository fakePlayersRepository = new InMemoryPlayersRepository();

    FindOrCreateUserUseCase findOrCreateUserUseCase = new FindOrCreateUserUseCase(
      getAllUsersFailure,
      fakeUserRepository,
      fakePlayersRepository,
      fakePlayersRepository
    );

    ProblemDetected error = assertThrows(
      ProblemDetected.class,
      () -> findOrCreateUserUseCase.apply(findOrCreateUserRequest)
    );

    assertThat(error.problem()).isEqualTo(PersistenceProblem.READ_FAILED);
  }

  @Test
  void apply_addUserFails_persistenceProblem() {
    Name nateSweeney = Name.resultOf("Nate Sweeney").get();
    Email email = Email.resultOf("nate@sweeney.org").get();
    var findOrCreateUserRequest = createFindOrCreateUserRequest(nateSweeney, email);

    InMemoryUsersRepository fakeUserRepository = new InMemoryUsersRepository();
    InMemoryPlayersRepository fakePlayersRepository = new InMemoryPlayersRepository();
    AddUser addUserFailure = (_1, _2) -> Either.left(PersistenceProblem.INSERT_FAILED);

    FindOrCreateUserUseCase findOrCreateUserUseCase = new FindOrCreateUserUseCase(
      fakeUserRepository,
      addUserFailure,
      fakePlayersRepository,
      fakePlayersRepository
    );

    ProblemDetected error = assertThrows(
      ProblemDetected.class,
      () -> findOrCreateUserUseCase.apply(findOrCreateUserRequest)
    );

    assertThat(error.problem()).isEqualTo(PersistenceProblem.INSERT_FAILED);
  }

  private FindOrCreateUserInput.Request createFindOrCreateUserRequest(Name name, Email email) {
    IntendedUser intendedUser = IntendedUser.anIntendedUser()
      .withName(name)
      .withEmail(email)
      .buildResult().get();
    return new FindOrCreateUserInput.Request(intendedUser);
  }
}
