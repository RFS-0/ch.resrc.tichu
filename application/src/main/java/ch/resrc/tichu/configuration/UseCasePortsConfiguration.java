package ch.resrc.tichu.configuration;

import ch.resrc.tichu.domain.repositories.GameRepository;
import ch.resrc.tichu.endpoints.games.ports.outbound.CreatedGameOutputSender;
import ch.resrc.tichu.eventbus.EventBus;
import ch.resrc.tichu.usecases.create_game.CreateGameUseCase;
import ch.resrc.tichu.usecases.create_game.CreateGameUseCase.OutboundPorts;
import ch.resrc.tichu.usecases.create_game.ports.inbound.CreateGame;
import javax.enterprise.context.ApplicationScoped;

public class UseCasePortsConfiguration {

  @ApplicationScoped
  public CreateGame createGame(GameRepository gameRepository, EventBus eventBus, CreatedGameOutputSender createdGameOutputSender) {
    return CreateGameUseCase.create(
      new OutboundPorts(
        gameRepository,
        eventBus,
        createdGameOutputSender
      )
    );
  }
}
