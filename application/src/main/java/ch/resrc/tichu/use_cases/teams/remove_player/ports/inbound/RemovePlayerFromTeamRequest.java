package ch.resrc.tichu.use_cases.teams.remove_player.ports.inbound;

import ch.resrc.tichu.use_cases.teams.remove_player.ports.documents.IntendedPlayerRemoval;

public class RemovePlayerFromTeamRequest {

  private final IntendedPlayerRemoval intent;

  public RemovePlayerFromTeamRequest(IntendedPlayerRemoval intent) {
    this.intent = intent;
  }

  public IntendedPlayerRemoval intent() {
    return intent;
  }

}
