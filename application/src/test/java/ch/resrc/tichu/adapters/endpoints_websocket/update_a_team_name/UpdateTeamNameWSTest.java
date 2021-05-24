package ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketClient;
import ch.resrc.tichu.adapters.endpoints_websocket.common.dto.GameDto;
import ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name.dto.IntendedTeamNameDto;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.JoinCode;
import ch.resrc.tichu.domain.value_objects.Name;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
class UpdateTeamNameWSTest {

  private static final String GAME_ID = "ea314fde-a1d4-44dd-92a0-6cd1f0a1f08d";
  private static final String LEFT_TEAM_ID = "eff532e9-177a-45bb-9f22-5522ee16e2ce";
  private static final String RIGHT_TEAM_ID = "7b6edbdd-386e-40f7-8dd4-ee2586d2483d";
  private static final String UPDATED_TEAM_NAME = "School of the One Thousand Hippos";

  private final Json json;

  @TestHTTPResource("/events/games/update-team-name/" + GAME_ID)
  private URI updateTeamName;
  @TestHTTPResource("/events/games/updated-team-name/" + GAME_ID)
  private URI updatedTeamName;

  public UpdateTeamNameWSTest(Json json) {
    this.json = json;
  }

  @BeforeAll
  public static void setup() {
    GetAllGames getAllGamesMock = UpdateTeamNameWSTest::existingGames;
    GetAllTeams getAllTeamsMock = UpdateTeamNameWSTest::existingTeams;

    QuarkusMock.installMockForType(getAllGamesMock, GetAllGames.class);
    QuarkusMock.installMockForType(getAllTeamsMock, GetAllTeams.class);
  }

  @Test
  void onMessage_validIntendedTeamName_usecaseSuccessfullyExecutedAndResultReceivedByCreatedClient() throws DeploymentException, IOException {
    // given
    WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
    WebSocketClient updatedTeamName = new WebSocketClient();

    // when
    try (Session update = webSocketContainer.connectToServer(WebSocketClient.class, this.updateTeamName);
         Session updated = webSocketContainer.connectToServer(updatedTeamName, this.updatedTeamName)) {
      IntendedTeamNameDto intendedTeamNameDto = new IntendedTeamNameDto(GAME_ID, LEFT_TEAM_ID, UPDATED_TEAM_NAME);

      RemoteEndpoint.Basic basicRemote = update.getBasicRemote();
      basicRemote.sendText(json.toJsonString(intendedTeamNameDto));

      // then
      await().atMost(3, TimeUnit.SECONDS).until(updatedTeamNameMessageReceived(updatedTeamName));

      String sentMessage = updatedTeamName.getMessages().get(0);
      GameDto updatedGame = json.parse(sentMessage, GameDto.class);
      assertThat(updatedGame.id).isEqualTo(GAME_ID);
      assertThat(updatedGame.teams.map(team -> team.name)).contains(UPDATED_TEAM_NAME);
    }
  }

  private Callable<Boolean> updatedTeamNameMessageReceived(WebSocketClient createdClient) {
    return () -> createdClient.getMessages().size() > 0;
  }

  private static User existingUser() {
    Id userId = Id.resultOf("a5f60f6e-9778-4b17-8de5-1dbbc898197f").get();
    Name stickyMickey = Name.resultOf("Gerard Blackwell").get();
    Email email = Email.resultOf("gerard@blackwell.com").get();
    Instant createdAt = Instant.now();

    return User.create(userId, stickyMickey, email, createdAt).get();
  }

  private static Either<? extends Problem, Set<Team>> existingTeams() {
    Id leftTeamId = Id.resultOf(LEFT_TEAM_ID).get();
    Id rightTeamId = Id.resultOf(RIGHT_TEAM_ID).get();
    return Either.right(
      HashSet.of(
        Team.create(leftTeamId).get(),
        Team.create(rightTeamId).get()
      )
    );
  }

  private static Either<? extends Problem, Set<Game>> existingGames() {
    User user = existingUser();
    return Either.right(
      HashSet.of(
        Game.create(
          Id.resultOf(GAME_ID).get(),
          existingUser(),
          JoinCode.next(),
          existingTeams().get(),
          HashSet.empty(),
          Instant.now()
        ).get()
      )
    );
  }
}
