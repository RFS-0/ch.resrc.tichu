package ch.resrc.tichu.adapters.endpoints_websocket.update_round;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.update_round.input.UpdateRoundWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.update_round.output.UpdateRoundWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.update_round.ports.input.UpdateRoundInput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.UPDATE_ROUND)
public class UpdateRoundWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final UpdateRoundInput updateRoundInput;
  private final UpdatedRoundWS updated;
  private final Json json;

  public UpdateRoundWS(UpdateRoundInput updateRoundInput,
                       UpdatedRoundWS updated,
                       Json json) {
    this.updateRoundInput = updateRoundInput;
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
    UpdateRoundWebSocketInput input;
    try {
      input = new UpdateRoundWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    final var useCaseResponse = updateRoundInput.apply(input.request());
    final var output = new UpdateRoundWebSocketOutput(json, useCaseResponse);
    updated.send(output);
  }
}
