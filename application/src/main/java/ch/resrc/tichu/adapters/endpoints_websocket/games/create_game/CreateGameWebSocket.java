package ch.resrc.tichu.adapters.endpoints_websocket.games.create_game;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Games;
import ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.input.CreateGameWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.output.CreatedGameWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.input.CreateGameInput;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.output.CreateGameOutput;
import io.vertx.core.impl.ConcurrentHashSet;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;

@ServerEndpoint(Games.CREATE)
public class CreateGameWebSocket {

  private static final Set<Session> SESSIONS = new ConcurrentHashSet<>();

  private final CreateGameInput createGame;
  private final CreatedGameWebSocket created;

  private final Json json;

  public CreateGameWebSocket(
    CreateGameInput createGame,
    CreatedGameWebSocket created,
    Json json) {
    this.createGame = createGame;
    this.created = created;
    this.json = json;
  }

  @OnOpen
  public void onOpen(Session session) {
    SESSIONS.add(session);
  }

  @OnMessage
  public void onMessage(String message) {
    CreateGameWebSocketInput input;
    try {
      input = new CreateGameWebSocketInput(json, message);
    } catch (InvalidInputDetected bad) {
      // TODO: send error message
      return;
    }
    CreateGameOutput.Response useCaseResponse = createGame.apply(input.request());
    CreatedGameWebSocketOutput output = new CreatedGameWebSocketOutput(json, useCaseResponse);
    created.send(output);
  }
}
