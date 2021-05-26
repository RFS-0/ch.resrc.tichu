package ch.resrc.tichu.adapters.endpoints_websocket.remove_first_player_from_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.remove_first_player_from_team.input.RemoveFirstPlayerFromTeamWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.remove_first_player_from_team.output.RemoveFirstPlayerFromTeamWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.remove_first_player_from_team.ports.input.RemoveFirstPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.remove_first_player_from_team.ports.output.RemoveFirstPlayerFromTeamOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.REMOVE_FIRST_PLAYER_FROM_TEAM)
public class RemoveFirstPlayerFromTeamWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final RemoveFirstPlayerFromTeamInput removeFirstPlayerFromTeamInput;
  private final RemovedFirstPlayerFromTeamWS removed;
  private final Json json;

  public RemoveFirstPlayerFromTeamWS(RemoveFirstPlayerFromTeamInput removeFirstPlayerFromTeamInput, RemovedFirstPlayerFromTeamWS removed, Json json) {
    this.removeFirstPlayerFromTeamInput = removeFirstPlayerFromTeamInput;
    this.removed = removed;
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
    RemoveFirstPlayerFromTeamWebSocketInput input;
    try {
      input = new RemoveFirstPlayerFromTeamWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    RemoveFirstPlayerFromTeamOutput.Response useCaseResponse = removeFirstPlayerFromTeamInput.apply(input.request());
    RemoveFirstPlayerFromTeamWebSocketOutput output = new RemoveFirstPlayerFromTeamWebSocketOutput(json, useCaseResponse);
    removed.send(output);
  }
}
