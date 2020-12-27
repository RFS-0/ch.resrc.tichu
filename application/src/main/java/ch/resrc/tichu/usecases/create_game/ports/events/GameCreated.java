package ch.resrc.tichu.usecases.create_game.ports.events;

import static ch.resrc.tichu.usecases.create_game.ports.documents.GameDocument.aGameDocument;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.usecases.create_game.ports.documents.GameDocument;

public class GameCreated extends Event {

  private final Game game;

  private GameCreated(Game game) {
    super();
    this.game = game;
  }

  public static GameCreated of(Game created) {
    return new GameCreated(created);
  }

  public GameDocument asDocument() {
    return aGameDocument()
      .withId(game.id())
      .withJoinCode(game.joinCode())
      .withTeam(game.team())
      .withOtherTeam(game.otherTeam())
      .withRounds(game.rounds())
      .withCreatedAt(game.createdAt())
      .withFinishedAt(game.finishedAt())
      .build();
  }
}
