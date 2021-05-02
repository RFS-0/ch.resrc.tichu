package ch.resrc.tichu.adapters.endpoints_websocket.games.create_a_game;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.games.create_a_game.input.CreateGameWebSocketInput;
import ch.resrc.tichu.adapters.endpoints_websocket.games.create_a_game.output.CreatedGameWebSocketOutput;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.input.CreateGameInput;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.output.CreateGameOutput;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint(WebSocketAddresses.Games.CREATE)
public class CreateGameWS {

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final CreateGameInput createGame;
  private final CreatedGameWS created;

  private final Json json;

  public CreateGameWS(CreateGameInput createGame,
                      CreatedGameWS created,
                      Json json) {
    this.createGame = createGame;
    this.created = created;
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
