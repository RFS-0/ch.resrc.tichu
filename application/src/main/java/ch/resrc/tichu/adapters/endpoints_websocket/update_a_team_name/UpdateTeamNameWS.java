package ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name.input.UpdateTeamNameWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name.output.UpdateTeamNameWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.update_a_team_name.ports.input.UpdateTeamNameInput;
import ch.resrc.tichu.use_cases.update_a_team_name.ports.output.UpdateTeamNameOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.Games.UPDATE_TEAM_NAME)
public class UpdateTeamNameWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final UpdateTeamNameInput updateTeamName;
  private final UpdatedTeamNameWS updated;

  private final Json json;

  public UpdateTeamNameWS(UpdateTeamNameInput updateTeamName,
                          UpdatedTeamNameWS updated,
                          Json json) {
    this.updateTeamName = updateTeamName;
    this.updated = updated;
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
    UpdateTeamNameWebSocketInput input;
    try {
      input = new UpdateTeamNameWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    UpdateTeamNameOutput.Response useCaseResponse = updateTeamName.apply(input.request());
    UpdateTeamNameWebSocketOutput output = new UpdateTeamNameWebSocketOutput(json, useCaseResponse);
    updated.send(output);
  }
}
