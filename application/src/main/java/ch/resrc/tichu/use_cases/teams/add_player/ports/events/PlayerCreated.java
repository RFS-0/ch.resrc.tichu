package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.use_cases.ports.documents.PlayerDocument;
import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.IntendedPlayerAddition;

import static ch.resrc.tichu.use_cases.ports.documents.PlayerDocument.aPlayerDocument;

public class PlayerCreated extends Event {

  private final IntendedPlayerAddition intent;
  private final Player player;

  private PlayerCreated(IntendedPlayerAddition intent, Player player) {
    this.intent = intent;
    this.player = player;
  }

  public static PlayerCreated of(IntendedPlayerAddition intent, Player player) {
    return new PlayerCreated(intent, player);
  }

  public Player player() {
    return player;
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }

  public PlayerDocument asDocument() {
    return aPlayerDocument()
      .withId(player.id())
      .withName(player.name())
      .withCreatedAt(player.createdAt())
      .build();
  }

}
