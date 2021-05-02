package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;

public class CreateAnonymousPlayer extends Event {

  private final IntendedPlayerAddition intent;

  private CreateAnonymousPlayer(IntendedPlayerAddition intent) {
    this.intent = intent;
  }

  public static CreateAnonymousPlayer of(IntendedPlayerAddition intent) {
    return new CreateAnonymousPlayer(intent);
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }
}
