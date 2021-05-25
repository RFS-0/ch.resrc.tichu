package ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team.input.AddFirstPlayerToTeamWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team.output.AddFirstPlayerToTeamWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.add_first_player_to_team.ports.input.AddFirstPlayerToTeamInput;
import ch.resrc.tichu.use_cases.add_first_player_to_team.ports.output.AddFirstPlayerToTeamOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.ADD_FIRST_PLAYER_TO_TEAM)
public class AddFirstPlayerToTeamWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final AddFirstPlayerToTeamInput addFirstPlayerToTeamInput;
  private final AddedFirstPlayerToTeamWS added;
  private final Json json;

  public AddFirstPlayerToTeamWS(AddFirstPlayerToTeamInput addFirstPlayerToTeamInput, AddedFirstPlayerToTeamWS added, Json json) {
    this.addFirstPlayerToTeamInput = addFirstPlayerToTeamInput;
    this.added = added;
    this.json = json;
  }

  @OnOpen
  public void onOpen(Session session) {
    SESSIONS_REF.updateAndGet(sessions -> sessions.add(session));
  }

  @OnClose
  public void onClose(Session session) {
    SESSIONS_REF.updateAndGet(sessions -> sessions.remove(session));
  }

  @OnMessage
  public void onMessage(String message) {
    AddFirstPlayerToTeamWebSocketInput input;
    try {
      input = new AddFirstPlayerToTeamWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    AddFirstPlayerToTeamOutput.Response useCaseResponse = addFirstPlayerToTeamInput.apply(input.request());
    AddFirstPlayerToTeamWebSocketOutput output = new AddFirstPlayerToTeamWebSocketOutput(json, useCaseResponse);
    added.send(output);
  }
}
