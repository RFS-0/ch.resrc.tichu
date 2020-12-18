package ch.resrc.tichu.use_cases.teams.remove_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.documents.IntendedPlayerRemoval;

public class RemovePlayerFromTeam extends Event {

  private final IntendedPlayerRemoval intent;

  private RemovePlayerFromTeam(IntendedPlayerRemoval intent) {
    this.intent = intent;
  }

  public static RemovePlayerFromTeam of(IntendedPlayerRemoval intent) {
    return new RemovePlayerFromTeam(intent);
  }

  public IntendedPlayerRemoval intent() {
    return intent;
  }
}
