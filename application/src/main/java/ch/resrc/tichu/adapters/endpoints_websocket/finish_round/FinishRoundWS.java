package ch.resrc.tichu.adapters.endpoints_websocket.finish_round;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.finish_round.input.FinishRoundWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.finish_round.output.FinishRoundWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.finish_round.ports.input.FinishRoundInput;
import ch.resrc.tichu.use_cases.finish_round.ports.output.FinishRoundOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.FINISH_ROUND)
public class FinishRoundWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final FinishRoundInput finishRoundInput;
  private final FinishedRoundWS updated;
  private final Json json;

  public FinishRoundWS(FinishRoundInput finishRoundInput,
                       FinishedRoundWS updated,
                       Json json) {
    this.finishRoundInput = finishRoundInput;
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
    FinishRoundWebSocketInput input;
    try {
      input = new FinishRoundWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    FinishRoundOutput.Response useCaseResponse = finishRoundInput.apply(input.request());
    FinishRoundWebSocketOutput output = new FinishRoundWebSocketOutput(json, useCaseResponse);
    updated.send(output);
  }
}
