package ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player.input.UpdateRankOfPlayerWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player.output.UpdateRankOfPlayerWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.UpdateRankOfPlayerInput;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.output.UpdateRankOfPlayerOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Input.UPDATE_RANK_OF_PLAYER)
public class UpdateRankOfPlayerWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final UpdateRankOfPlayerInput updateRankOfPlayer;
  private final UpdatedRankOfPlayerWS updated;
  private final Json json;

  public UpdateRankOfPlayerWS(UpdateRankOfPlayerInput updateRankOfPlayer, UpdatedRankOfPlayerWS updated, Json json) {
    this.updateRankOfPlayer = updateRankOfPlayer;
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
    UpdateRankOfPlayerWebSocketInput input;
    try {
      input = new UpdateRankOfPlayerWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    UpdateRankOfPlayerOutput.Response useCaseResponse = updateRankOfPlayer.apply(input.request());
    UpdateRankOfPlayerWebSocketOutput output = new UpdateRankOfPlayerWebSocketOutput(json, useCaseResponse);
    updated.send(output);
  }
}
