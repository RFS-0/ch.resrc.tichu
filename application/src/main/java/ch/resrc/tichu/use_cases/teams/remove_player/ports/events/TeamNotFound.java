package ch.resrc.tichu.use_cases.teams.remove_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.documents.IntendedPlayerRemoval;

public class TeamNotFound extends Event {

  private final IntendedPlayerRemoval intent;

  private TeamNotFound(IntendedPlayerRemoval intent) {
    this.intent = intent;
  }

  public static TeamNotFound of(IntendedPlayerRemoval intent) {
    return new TeamNotFound(intent);
  }

  public IntendedPlayerRemoval intent() {
    return intent;
  }

}
