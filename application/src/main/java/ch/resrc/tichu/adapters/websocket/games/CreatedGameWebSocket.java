package ch.resrc.tichu.adapters.websocket.games;

import static ch.resrc.tichu.capabilities.errorhandling.ErrorAudit.reportProblemTo;

import ch.resrc.tichu.adapters.websocket.games.dto.GameDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.json.JsonBody;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.endpoints.errorhandling.UseCaseTry;
import ch.resrc.tichu.endpoints.games.ports.outbound.CreatedGameOutputSender;
import ch.resrc.tichu.endpoints.websockets.WebSocketsAddresses.Games;
import ch.resrc.tichu.eventbus.EventBus;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(Games.CREATED)
public class CreatedGameWebSocket implements CreatedGameOutputSender {

  private static final Logger LOG = LoggerFactory.getLogger(CreatedGameWebSocket.class);

  private static final Map<Id, Set<Session>> SESSIONS = new ConcurrentHashMap<>();

  private final Json json;
  private final UseCaseTry useCaseTry;

  public CreatedGameWebSocket(Json json, EventBus eventBus) {
    this.json = json;
    this.useCaseTry = new UseCaseTry(reportProblemTo(LOG, eventBus));
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("userId") String userId) {
    System.out.println("Web socket handling request is the following -> " + this);
    final var theId = Id.resultOf(userId).getOrThrow(InvalidInputDetected::of);
    SESSIONS.computeIfAbsent(theId, k -> new HashSet<>());
    SESSIONS.get(theId).add(session);
  }

  @OnClose
  public void onClose(Session session, @PathParam("userId") String userId) {
    final var theId = Id.resultOf(userId).getOrThrow(InvalidInputDetected::of);
    SESSIONS.get(theId).remove(session);
  }

  @OnError
  public void onError(Session session, @PathParam("userId") String userId, Throwable throwable) {
    final var theId = Id.resultOf(userId).getOrThrow(InvalidInputDetected::of);
    SESSIONS.get(theId).remove(session);
  }

  @Override
  public void send(Id userId, JsonBody<GameDto> message) {
    System.out.println("Web socket handling request is the following -> " + this);
    if (userId == null) {
      LOG.error("Can not send message to unknown user. You have to provide a user id.");
    } else if (message == null || message.content() == null || message.content().isEmpty()) {
      LOG.error("Can not send empty message. You have to provide a message.");
    } else if (SESSIONS.get(userId) == null) {
      LOG.error("Could not retrieve session for specified user id. You have to make sure ");
    } else {
      SESSIONS.get(userId).forEach(session -> session.getAsyncRemote().sendText(message.content()));
    }
  }
}
