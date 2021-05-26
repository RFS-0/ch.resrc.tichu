package ch.resrc.tichu.adapters.endpoints_websocket.remove_second_player_from_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.remove_second_player_from_team.input.RemoveSecondPlayerFromTeamWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.remove_second_player_from_team.output.RemoveSecondPlayerFromTeamWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.input.RemoveSecondPlayerFromTeamInput;
import ch.resrc.tichu.use_cases.remove_second_player_from_team.ports.output.RemoveSecondPlayerFromTeamOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.REMOVE_SECOND_PLAYER_FROM_TEAM)
public class RemoveSecondPlayerFromTeamWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final RemoveSecondPlayerFromTeamInput removeSecondPlayerFromTeamInput;
  private final RemovedSecondPlayerFromTeamWS removed;
  private final Json json;

  public RemoveSecondPlayerFromTeamWS(RemoveSecondPlayerFromTeamInput removeSecondPlayerFromTeamInput, RemovedSecondPlayerFromTeamWS removed, Json json) {
    this.removeSecondPlayerFromTeamInput = removeSecondPlayerFromTeamInput;
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
    RemoveSecondPlayerFromTeamWebSocketInput input;
    try {
      input = new RemoveSecondPlayerFromTeamWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    RemoveSecondPlayerFromTeamOutput.Response useCaseResponse = removeSecondPlayerFromTeamInput.apply(input.request());
    RemoveSecondPlayerFromTeamWebSocketOutput output = new RemoveSecondPlayerFromTeamWebSocketOutput(json, useCaseResponse);
    removed.send(output);
  }
}
