package ch.resrc.tichu.adapters.endpoints_websocket.games.create_game;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketClient;
import ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.dto.GameDto;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
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
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
class CreateGameWebSocketTest {

  @TestHTTPResource(WebSocketAddresses.Games.CREATE)
  URI create;
  @TestHTTPResource("/events/games/created/07fa58f3-5026-4bfd-92e7-adf2bd6b8c8f")
  URI created;

  private final Json json;

  public CreateGameWebSocketTest(Json json) {
    this.json = json;
  }

  @BeforeAll
  public static void setup() {
    GetAllUsers allUsersMock = CreateGameWebSocketTest::createUsers;
    QuarkusMock.installMockForType(allUsersMock, GetAllUsers.class);
  }

  @Test
  void onMessage_validIntendedGame_usecaseSuccessfullyExecutedAndResultReceivedByCreatedClient() throws Exception {
    // given
    WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
    WebSocketClient createdClient = new WebSocketClient();

    // when
    try (Session create = webSocketContainer.connectToServer(WebSocketClient.class, this.create);
         Session created = webSocketContainer.connectToServer(createdClient, this.created)) {
      IntendedGameDto intendedGameDto = new IntendedGameDto("07fa58f3-5026-4bfd-92e7-adf2bd6b8c8f");
      RemoteEndpoint.Basic basicRemote = create.getBasicRemote();
      basicRemote.sendText(json.toJsonString(intendedGameDto));


      // then
      await().atMost(3, TimeUnit.SECONDS).until(createdMessageReceived(createdClient));

      String sentMessage = createdClient.getMessages().get(0);
      GameDto createdGame = json.parse(sentMessage, GameDto.class);
      assertThat(createdGame.createdBy.id).isEqualTo("07fa58f3-5026-4bfd-92e7-adf2bd6b8c8f");
    }
  }

  private Callable<Boolean> createdMessageReceived(WebSocketClient createdClient) {
    return () -> createdClient.getMessages().size() > 0;
  }

  private static Either<? extends Problem, Set<User>> createUsers() {
    Id createdByUser = Id.resultOf("07fa58f3-5026-4bfd-92e7-adf2bd6b8c8f").get();
    Name stickyMickey = Name.resultOf("Sticky Mickey").get();
    Email email = Email.resultOf("sticky@mickey.com").get();
    Instant createdAt = Instant.now();
    User existingUser = User.create(createdByUser, stickyMickey, email, createdAt).get();

    return Either.right(HashSet.of(existingUser)
    );
  }
}
