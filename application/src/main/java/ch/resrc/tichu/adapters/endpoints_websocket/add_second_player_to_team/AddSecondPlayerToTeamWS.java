package ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team.input.AddSecondPlayerToTeamWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team.output.AddSecondPlayerToTeamWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.add_second_player_to_team.ports.input.AddSecondPlayerToTeamInput;
import ch.resrc.tichu.use_cases.add_second_player_to_team.ports.output.AddSecondPlayerToTeamOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.ADD_SECOND_PLAYER_TO_TEAM)
public class AddSecondPlayerToTeamWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final AddSecondPlayerToTeamInput addSecondPlayerToTeamInput;
  private final AddedSecondPlayerToTeamWS added;
  private final Json json;

  public AddSecondPlayerToTeamWS(AddSecondPlayerToTeamInput addSecondPlayerToTeamInput, AddedSecondPlayerToTeamWS added, Json json) {
    this.addSecondPlayerToTeamInput = addSecondPlayerToTeamInput;
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
    AddSecondPlayerToTeamWebSocketInput input;
    try {
      input = new AddSecondPlayerToTeamWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    AddSecondPlayerToTeamOutput.Response useCaseResponse = addSecondPlayerToTeamInput.apply(input.request());
    AddSecondPlayerToTeamWebSocketOutput output = new AddSecondPlayerToTeamWebSocketOutput(json, useCaseResponse);
    added.send(output);
  }
}
