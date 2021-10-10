package ch.resrc.old.adapters.endpoints_websocket;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;

import javax.websocket.*;
import javax.websocket.server.*;
import java.util.concurrent.atomic.*;

@ServerEndpoint(WebSocketAddresses.Output.USE_CASE_OUTPUT)
public class UseCaseOutputSocket {

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

  public void send(Id receiverId, String output) {
    Map<Id, Set<Session>> idToSessions = ID_TO_SESSIONS_REF.get();
    for (Set<Session> sessions : idToSessions.get(receiverId)) {
      for (Session session : sessions) {
        session.getAsyncRemote().sendText(output);
      }
    }
  }
}
