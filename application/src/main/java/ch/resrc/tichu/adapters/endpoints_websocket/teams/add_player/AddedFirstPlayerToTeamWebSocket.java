package ch.resrc.tichu.adapters.endpoints_websocket.teams.add_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.domain.value_objects.Id;

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

@ServerEndpoint(Teams.ADDED_FIRST_PLAYER_TO_TEAM)
public class AddedFirstPlayerToTeamWebSocket {

  private static final Map<Id, Set<Session>> SESSIONS = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, @PathParam("teamId") String teamId) {
    Id.resultOf(teamId)
      .mapLeft(InvalidInputDetected::of)
      .peek(id -> SESSIONS.computeIfAbsent(id, k -> new HashSet<>()))
      .peek(id -> SESSIONS.get(id).add(session));
  }

  @OnClose
  public void onClose(Session session, @PathParam("teamId") String teamId) {
    final var theId = Id.resultOf(teamId).getOrElseThrow(InvalidInputDetected::of);
    SESSIONS.get(theId).remove(session);
  }

  @OnError
  public void onError(Session session, @PathParam("teamId") String teamId, Throwable throwable) {
    final var theId = Id.resultOf(teamId).getOrElseThrow(InvalidInputDetected::of);
    SESSIONS.get(theId).remove(session);
  }

}
