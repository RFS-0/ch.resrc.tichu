package ch.resrc.tichu.adapters.endpoints_websocket.teams.remove_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.adapters.endpoints_websocket.teams.find_by_id.FindTeamByIdWebSocket;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.domain.value_objects.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(Teams.REMOVED_SECOND_PLAYER_FROM_TEAM)
public class RemovedSecondPlayerFromTeamWebSocket {

  private static final Logger LOG = LoggerFactory.getLogger(FindTeamByIdWebSocket.class);
  private static final Map<Id, Set<Session>> SESSIONS = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, @PathParam("receiverId") String receiverId) {
    final var theId = Id.resultOf(receiverId).getOrElseThrow(InvalidInputDetected::of);
    SESSIONS.computeIfAbsent(theId, k -> new HashSet<>());
    SESSIONS.get(theId).add(session);
  }

  @OnClose
  public void onClose(Session session, @PathParam("receiverId") String receiverId) {
    final var theId = Id.resultOf(receiverId).getOrElseThrow(InvalidInputDetected::of);
    SESSIONS.get(theId).remove(session);
  }

  @OnError
  public void onError(Session session, @PathParam("receiverId") String receiverId, Throwable throwable) {
    final var theId = Id.resultOf(receiverId).getOrElseThrow(InvalidInputDetected::of);
    SESSIONS.get(theId).remove(session);
  }
}
