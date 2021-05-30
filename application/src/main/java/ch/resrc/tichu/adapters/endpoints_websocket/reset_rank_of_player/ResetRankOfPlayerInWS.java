package ch.resrc.tichu.adapters.endpoints_websocket.reset_rank_of_player;

import ch.resrc.tichu.adapters.endpoints_websocket.reset_rank_of_player.input.ResetRankOfPlayerWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.reset_rank_of_player.output.ResetRankOfPlayerWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.ResetRankOfPlayerInput;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.output.ResetRankOfPlayerOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.util.concurrent.atomic.AtomicReference;

public class ResetRankOfPlayerInWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final ResetRankOfPlayerInput resetRankOfPlayer;
  private final ResetRankOfPlayerOutWS out;
  private final Json json;

  public ResetRankOfPlayerInWS(ResetRankOfPlayerInput resetRankOfPlayerInput, ResetRankOfPlayerOutWS out, Json json) {
    this.resetRankOfPlayer = resetRankOfPlayerInput;
    this.out = out;
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
    ResetRankOfPlayerWebSocketInput input;
    try {
      input = new ResetRankOfPlayerWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    ResetRankOfPlayerOutput.Response useCaseResponse = resetRankOfPlayer.apply(input.request());
    ResetRankOfPlayerWebSocketOutput output = new ResetRankOfPlayerWebSocketOutput(json, useCaseResponse);
    out.send(output);
  }
}
