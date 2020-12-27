package ch.resrc.tichu.adapters.websocket.games;

import static org.assertj.core.api.Assertions.assertThat;

import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.repositories.GameRepository;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.endpoints.websockets.WebSocketsAddresses.Games;
import ch.resrc.tichu.testcapabilities.endpoints.jsonfixtures.IntendedGameJson;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CreateGameWebSocketTest {

  private static final LinkedBlockingDeque<String> INPUT_MESSAGES = new LinkedBlockingDeque<>();
  private static final LinkedBlockingDeque<String> OUTPUT_MESSAGES = new LinkedBlockingDeque<>();

  private final GameRepository gameRepository;
  private final Id userId = Id.of("0db589fd-7fa2-4a6a-b4e1-0e6c033879cb");

  @TestHTTPResource(Games.CREATE)
  URI create;
  @TestHTTPResource("/games/created/0db589fd-7fa2-4a6a-b4e1-0e6c033879cb")
  URI created;

  public CreateGameWebSocketTest(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @Test
  public void onMessage_creates_theIntendedGame() throws Exception {
    // given
    final var theIntendedGame = new IntendedGameJson().but(theIntended -> theIntended.userId = userId.asLiteral());
    gameRepository.deleteAll();
    Session senderSession = ContainerProvider.getWebSocketContainer().connectToServer(OutputSender.class, created);
    Session receiverSession = ContainerProvider.getWebSocketContainer().connectToServer(InputReceiver.class, create);
    assertThat(INPUT_MESSAGES.poll(10, TimeUnit.SECONDS)).isEqualTo("CONNECT");
    assertThat(OUTPUT_MESSAGES.poll(10, TimeUnit.SECONDS)).isEqualTo("CONNECT");

    // when
    receiverSession.getAsyncRemote().sendText(theIntendedGame.toString());
    assertThat(OUTPUT_MESSAGES.poll(10, TimeUnit.SECONDS)).isNotEmpty();

    // then
    final Set<Game> allGames = gameRepository.all();
    assertThat(allGames).hasSize(1);
    assertThat(allGames).anyMatch(game -> game.team().players().contains(userId));
  }

  @ClientEndpoint
  public static class InputReceiver {

    @OnOpen
    public void open(Session session) {
      INPUT_MESSAGES.add("CONNECT");
    }

    @OnMessage
    void message(String msg) {
      INPUT_MESSAGES.add(msg);
    }
  }

  @ClientEndpoint
  public static class OutputSender {

    @OnOpen
    public void open(Session session) {
      OUTPUT_MESSAGES.add("CONNECT");
    }

    @OnMessage
    void message(String msg) {
      OUTPUT_MESSAGES.add(msg);
    }
  }
}
