package ch.resrc.tichu.adapters.endpoints_websocket.update_card_points_of_round;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.update_card_points_of_round.input.UpdateCardPointsOfRoundWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.update_card_points_of_round.output.UpdateCardPointsOfRoundWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.UpdateCardPointsOfRoundInput;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.output.UpdateCardPointsOfRoundOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.UPDATE_CARD_POINTS_OF_ROUND)
public class UpdateCardPointsOfRoundWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final UpdateCardPointsOfRoundInput updateCardPoints;
  private final UpdatedCardPointsOfRoundWS updated;
  private final Json json;

  public UpdateCardPointsOfRoundWS(UpdateCardPointsOfRoundInput updateCardPoints,
                                   UpdatedCardPointsOfRoundWS updated,
                                   Json json) {
    this.updateCardPoints = updateCardPoints;
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
    UpdateCardPointsOfRoundWebSocketInput input;
    try {
      input = new UpdateCardPointsOfRoundWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    UpdateCardPointsOfRoundOutput.Response useCaseResponse = updateCardPoints.apply(input.request());
    UpdateCardPointsOfRoundWebSocketOutput output = new UpdateCardPointsOfRoundWebSocketOutput(json, useCaseResponse);
    updated.send(output);
  }
}
