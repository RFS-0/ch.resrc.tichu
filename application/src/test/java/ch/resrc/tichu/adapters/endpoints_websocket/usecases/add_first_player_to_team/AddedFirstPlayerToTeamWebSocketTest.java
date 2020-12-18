package ch.resrc.tichu.adapters.endpoints_websocket.usecases.add_first_player_to_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketClient;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

@QuarkusTest
class AddedFirstPlayerToTeamWebSocketTest {

  @TestHTTPResource(Teams.ADD_FIRST_PLAYER_TO_TEAM)
  URI add;
  @TestHTTPResource("/events/teams/added-first-player-to-team/35b25c71-318e-45cd-bfd8-f8b06fae37de")
  URI added;

  @Test
  public void onMessage_addFirstPlayerToTeam() throws DeploymentException, IOException, InterruptedException {
    // TODO: setup fixture to properly test this
    // given
    WebSocketClient addClient = new WebSocketClient();
    WebSocketClient addedClient = new WebSocketClient();
    Session senderSession = ContainerProvider.getWebSocketContainer().connectToServer(addedClient, added);
    Session receiverSession = ContainerProvider.getWebSocketContainer().connectToServer(addClient, add);
    senderSession.getAsyncRemote().sendText("CONNECT-1");
    senderSession.getBasicRemote().sendText("CONNECT-2");

    WebSocketContainer container = senderSession.getContainer();

    // when

    // then
  }
}
