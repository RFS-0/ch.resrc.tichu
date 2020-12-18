package ch.resrc.tichu.use_cases.teams.add_player.ports.inbound;

import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.IntendedPlayerAddition;

public class AddPlayerToTeamRequest {

  private final IntendedPlayerAddition intent;

  public AddPlayerToTeamRequest(IntendedPlayerAddition intent) {
    this.intent = intent;
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }

}
