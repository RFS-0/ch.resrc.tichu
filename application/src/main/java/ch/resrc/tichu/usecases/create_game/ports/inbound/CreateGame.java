package ch.resrc.tichu.usecases.create_game.ports.inbound;

import ch.resrc.tichu.adapters.websocket.games.output.CreateGameOutput;
import ch.resrc.tichu.usecases.UseCasePort;
import ch.resrc.tichu.usecases.create_game.ports.documents.IntendedGame;

/**
 * Creates new games.
 */
@FunctionalInterface
public interface CreateGame extends UseCasePort {

  /**
   * Creates a new game on behalf of a user.
   *
   * @param requested specifies the details how the time bin should be created
   */
  void invoke(Request requested, CreateGameOutput out);

  class Request {

    private final IntendedGame intent;

    public Request(IntendedGame intent) {
      this.intent = intent;
    }

    public IntendedGame intent() {
      return intent;
    }
  }
}
