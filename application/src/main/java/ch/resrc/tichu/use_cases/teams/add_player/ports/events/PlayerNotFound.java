package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.IntendedPlayerAddition;

public class PlayerNotFound extends Event {

  private final IntendedPlayerAddition intent;

  private PlayerNotFound(IntendedPlayerAddition intent) {
    this.intent = intent;
  }

  public static PlayerNotFound of(IntendedPlayerAddition intent) {
    return new PlayerNotFound(intent);
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }
}
