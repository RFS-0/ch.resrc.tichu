package ch.resrc.tichu.use_cases.find_or_create_user;

import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryPlayersRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryUsersRepository;
import ch.resrc.tichu.capabilities.errorhandling.PersistenceProblem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDetected;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.FindOrCreateUserInput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.IntendedUser;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.FindOrCreateUserOutput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.UserDocument;
import io.vavr.collection.HashSet;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
