package ch.resrc.tichu.configuration;

import ch.resrc.tichu.adapters.websocket.games.CreateGameWebSocket;
import ch.resrc.tichu.adapters.websocket.games.CreatedGameWebSocket;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.endpoints.games.ports.inbound.CreateGameInputReceiver;
import ch.resrc.tichu.endpoints.games.ports.outbound.CreatedGameOutputSender;
import ch.resrc.tichu.eventbus.EventBus;
import ch.resrc.tichu.usecases.create_game.ports.inbound.CreateGame;
import io.quarkus.arc.AlternativePriority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;

@Dependent
public class WebsocketConfiguration {

  @ApplicationScoped
  CreateGameInputReceiver createGameMessageSource(
    CreateGame createGame,
    CreatedGameOutputSender sender,
    ProblemCatalogue problemCatalogue,
    Json json,
    EventBus eventBus
  ) {
    return new CreateGameWebSocket(createGame, sender, problemCatalogue, json, eventBus);
  }

  @AlternativePriority(value = Integer.MAX_VALUE)
  CreatedGameOutputSender createdGameMessageSink(Json json, EventBus eventBus) {
    return new CreatedGameWebSocket(json, eventBus);
  }
}
