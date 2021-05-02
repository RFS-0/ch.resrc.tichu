package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;

public class TeamNotFound extends Event {

  private final IntendedPlayerAddition intent;
  private final Player player;

  private TeamNotFound(IntendedPlayerAddition intent, Player player) {
    this.intent = intent;
    this.player = player;
  }

  public static TeamNotFound of(IntendedPlayerAddition intent, Player player) {
    return new TeamNotFound(intent, player);
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }

  public Player player() {
    return player;
  }
}
