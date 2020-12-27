package ch.resrc.tichu.adapters.websocket.games;

import static ch.resrc.tichu.capabilities.errorhandling.ErrorAudit.reportProblemTo;

import ch.resrc.tichu.adapters.websocket.games.dto.IntendedGameDto;
import ch.resrc.tichu.adapters.websocket.games.input.CreateGameInput;
import ch.resrc.tichu.adapters.websocket.games.output.CreateGameOutput;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.endpoints.errorhandling.UseCaseTry;
import ch.resrc.tichu.endpoints.games.ports.inbound.CreateGameInputReceiver;
import ch.resrc.tichu.endpoints.games.ports.outbound.CreatedGameOutputSender;
import ch.resrc.tichu.endpoints.websockets.WebSocketsAddresses.Games;
import ch.resrc.tichu.eventbus.EventBus;
import ch.resrc.tichu.usecases.create_game.ports.inbound.CreateGame;
import io.vertx.core.impl.ConcurrentHashSet;
import java.util.Set;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(Games.CREATE)
public class CreateGameWebSocket implements CreateGameInputReceiver {

  private static final Logger LOG = LoggerFactory.getLogger(CreateGameWebSocket.class);
  private static final Set<Session> SESSIONS = new ConcurrentHashSet<>();

  private final CreateGame createGame;
  private final CreatedGameOutputSender sender;

  private final ProblemCatalogue problemCatalogue;
  private final Json json;
  private final UseCaseTry useCaseTry;


  public CreateGameWebSocket(
    CreateGame createGame,
    CreatedGameOutputSender sender,
    ProblemCatalogue problemCatalogue,
    Json json,
    EventBus eventBus
  ) {
    this.createGame = createGame;
    this.sender = sender;
    this.problemCatalogue = problemCatalogue;
    this.json = json;
    this.useCaseTry = new UseCaseTry(reportProblemTo(LOG, eventBus));
  }

  @OnOpen
  public void onOpen(Session session) {
    SESSIONS.add(session);
  }

  @OnMessage
  @Override
  public void onMessage(String message) {
    final var intendedGameDto = json.parse(message, IntendedGameDto.class);
    final var createdGame = useCaseTry
      .withInput(new CreateGameInput(intendedGameDto))
      .withOutput(new CreateGameOutput(problemCatalogue, json))
      .invoke((in, out) -> createGame.invoke(in.request(), out))
      .documentPresentation();
    sender.send(Id.of(intendedGameDto.userId), createdGame);
  }
}
