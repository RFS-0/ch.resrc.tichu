package ch.resrc.tichu.usecases.create_game;

import static ch.resrc.tichu.capabilities.errorhandling.Blame.isClientFault;
import static ch.resrc.tichu.capabilities.functional.ForEach.forEach;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.Case;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.DefaultIgnore;
import static io.vavr.API.$;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import ch.resrc.tichu.adapters.websocket.games.output.CreateGameOutput;
import ch.resrc.tichu.capabilities.events.EventForwarding;
import ch.resrc.tichu.capabilities.events.Eventful;
import ch.resrc.tichu.capabilities.events.Notifiable;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Game.CreationError;
import ch.resrc.tichu.domain.repositories.GameRepository;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.endpoints.games.ports.outbound.CreatedGameOutputSender;
import ch.resrc.tichu.eventbus.EventBus;
import ch.resrc.tichu.usecases.UseCaseEnactment;
import ch.resrc.tichu.usecases.create_game.ports.documents.GameDocument;
import ch.resrc.tichu.usecases.create_game.ports.documents.IntendedGame;
import ch.resrc.tichu.usecases.create_game.ports.events.GameCreated;
import ch.resrc.tichu.usecases.create_game.ports.inbound.CreateGame;
import ch.resrc.tichu.usecases.create_game.ports.outbound.GamePresenter;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateGameUseCase implements CreateGame {

  private static final Logger LOG = LoggerFactory.getLogger(CreateGameUseCase.class);

  private final OutboundPorts ports;

  private CreateGameUseCase(OutboundPorts ports) {
    this.ports = ports;
  }

  ///// Roles /////

  static class Workflow extends Eventful implements EventForwarding {

    final GameSource gameSource;

    void createGame(IntendedGame theIntended) {

      gameSource.createGame(theIntended)
        .ifSuccess(NewGame::store)
        .ifSuccess((NewGame x) -> x.publishCreationSuccessTo(this))
        .ifFailure(forEach(this::publish));
    }

    Workflow(GameSource gameSource) {
      this.gameSource = gameSource;
      gameSource.events().subscribe(this);
    }
  }

  static class GameSource extends Eventful implements EventForwarding {

    final GameRepository repository;

    Result<NewGame, CreationError> createGame(IntendedGame theIntended) {
      Supplier<Result<NewGame, CreationError>> newGame = () -> newGameCreatedBy(theIntended.createdBy()).map(asNewGame());
      return newGame.get();
    }

    private Result<Game, Game.CreationError> newGameCreatedBy(Id userId) {
      return Game.createdBy(userId);
    }

    private Function<Game, NewGame> asNewGame() {
      return (Game x) -> new NewGame(x, repository);
    }

    GameSource(GameRepository repository) {
      this.repository = repository;
    }
  }

  static class NewGame {

    final Game self;
    final GameRepository repository;

    void store() {
      repository.insert(self);
    }

    void publishCreationSuccessTo(Notifiable receiver) {
      receiver.on(GameCreated.of(self));
    }

    public NewGame(Game self, GameRepository repository) {
      this.self = self;
      this.repository = repository;
    }
  }

  static class UserInterface implements Notifiable {

    final GamePresenter presenter;

    @Override
    public void on(Object event) {
      Match(event).of(
        Case($(instanceOf(GameCreated.class)), the -> this.presentGame(the.asDocument())),
        Case($(isClientFault()), presenter::presentBusinessError),
        DefaultIgnore()
      );
    }

    private void presentGame(GameDocument theCreatedGame) {
      presenter.present(theCreatedGame);
    }

    UserInterface(GamePresenter presenter) {
      this.presenter = presenter;
    }
  }

  ///// Enactment /////

  public static CreateGame create(OutboundPorts ports) {
    return new CreateGameUseCase(ports);
  }

  @Override
  public void invoke(Request requested, CreateGameOutput out) {

    new UseCaseEnactment() {

      public void invoke() {
        workflow.createGame(requested.intent());
      }

      final Workflow workflow;

      ///// Role Assignment /////
      {

        GameSource gameSource = new GameSource(ports.repository);

        workflow = new Workflow(gameSource);

        UserInterface ui = new UserInterface(out);

        workflow.events().subscribe(ui);
      }

    }.invoke();

  }

  ///// Outbound Ports /////

  public static class OutboundPorts {

    private final GameRepository repository;
    private final EventBus eventBus;
    private final CreatedGameOutputSender createdGameOutputSender;

    public OutboundPorts(GameRepository repository, EventBus eventBus,
      CreatedGameOutputSender createdGameOutputSender) {
      this.repository = repository;
      this.eventBus = eventBus;
      this.createdGameOutputSender = createdGameOutputSender;
    }
  }
}
