package ch.resrc.tichu.adapters.endpoints_websocket.finish_game;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.finish_game.input.FinishGameWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.finish_game.output.FinishGameWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.finish_game.ports.input.FinishGameInput;
import ch.resrc.tichu.use_cases.finish_game.ports.output.FinishGameOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.FINISH_GAME)
public class FinishGameWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final FinishGameInput finishGameInput;
  private final FinishedGameWS updated;
  private final Json json;

  public FinishGameWS(FinishGameInput finishGameInput,
                      FinishedGameWS updated,
                      Json json) {
    this.finishGameInput = finishGameInput;
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
    final FinishGameWebSocketInput input;
    try {
      input = new FinishGameWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    FinishGameOutput.Response useCaseResponse = finishGameInput.apply(input.request());
    FinishGameWebSocketOutput output = new FinishGameWebSocketOutput(json, useCaseResponse);
    updated.send(output);
  }
}
