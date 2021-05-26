package ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team.output.AddSecondPlayerToTeamWebSocketOutput;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.UseCases.Output.ADD_SECOND_PLAYER_TO_TEAM)
public class AddedSecondPlayerToTeamWS {

  private static final AtomicReference<Map<Id, Set<Session>>> ID_TO_SESSIONS_REF = new AtomicReference<>(HashMap.empty());

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

  public String send(AddSecondPlayerToTeamWebSocketOutput output) {
    Map<Id, Set<Session>> idToSessions = ID_TO_SESSIONS_REF.get();
    for (Set<Session> sessions : idToSessions.get(output.receiver())) {
      for (Session session : sessions) {
        session.getAsyncRemote().sendText(output.response());
      }
    }
    return output.response();
  }
}
