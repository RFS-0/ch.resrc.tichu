package ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player;

import ch.resrc.tichu.adapters.endpoints_websocket.update_a_team_name.UpdatedTeamNameWS;
import ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player.output.UpdateRankOfPlayerWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.update_a_team_name.ports.input.UpdateTeamNameInput;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.util.concurrent.atomic.AtomicReference;

public class UpdatedRankOfPlayerWS {

  private static final AtomicReference<Map<Id, Set<Session>>> ID_TO_SESSIONS_REF = new AtomicReference<>(HashMap.empty());

  public UpdatedRankOfPlayerWS(UpdateTeamNameInput updateTeamName, UpdatedTeamNameWS updated, Json json) {
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("receiverId") String receiverId) {
    Id receiver = Id.resultOf(receiverId).getOrElseThrow(InvalidInputDetected::of);
    ID_TO_SESSIONS_REF.updateAndGet(
      sessions -> sessions.computeIfAbsent(receiver, key -> HashSet.empty())._2
    );
    ID_TO_SESSIONS_REF.updateAndGet(
      sessions -> sessions.computeIfPresent(receiver, (key, idToSessions) -> idToSessions.add(session))._2
    );
  }

  @OnClose
  public void onClose(Session session, @PathParam("receiverId") String receiverId) {
    Id receiver = Id.resultOf(receiverId).getOrElseThrow(InvalidInputDetected::of);
    ID_TO_SESSIONS_REF.updateAndGet(sessions -> {
      Set<Session> closedSessionRemoved = sessions.get(receiver).get().remove(session);
      return sessions.put(receiver, closedSessionRemoved);
    });
  }

  @OnError
  public void onError(Session session, @PathParam("receiverId") String receiverId, Throwable throwable) {
    Id receiver = Id.resultOf(receiverId).getOrElseThrow(InvalidInputDetected::of);
    ID_TO_SESSIONS_REF.updateAndGet(sessions -> {
      Set<Session> closedSessionRemoved = sessions.get(receiver).get().remove(session);
      return sessions.put(receiver, closedSessionRemoved);
    });
  }

  public String send(UpdateRankOfPlayerWebSocketOutput output) {
    Map<Id, Set<Session>> idToSessions = ID_TO_SESSIONS_REF.get();
    for (Set<Session> sessions : idToSessions.get(output.receiver())) {
      for (Session session : sessions) {
        session.getAsyncRemote().sendText(output.response());
      }
    }
    return output.response();
  }
}
