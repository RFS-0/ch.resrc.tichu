package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;

public class SearchPlayer extends Event {

  private final IntendedPlayerAddition intent;

  private SearchPlayer(IntendedPlayerAddition intent) {
    this.intent = intent;
  }

  public static SearchPlayer of(IntendedPlayerAddition intent) {
    return new SearchPlayer(intent);
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }
}
