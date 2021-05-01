package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.use_cases.ports.documents.PlayerDocument;
import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.IntendedPlayerAddition;
import ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument;

import static ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument.aTeamDocument;

public class TeamFound extends Event {

  private final IntendedPlayerAddition intent;
  private final Player player;
  private final Team team;

  public TeamFound(IntendedPlayerAddition intent, Player player, Team team) {
    this.intent = intent;
    this.player = player;
    this.team = team;
  }

  public static TeamFound of(IntendedPlayerAddition intent, Player player, Team team) {
    return new TeamFound(intent, player, team);
  }

  public IntendedPlayerAddition intent() {
    return intent;
  }

  public Player player() {
    return player;
  }

  public Team team() {
    return team;
  }

  public TeamDocument asDocument() {
    return aTeamDocument()
      .withId(team.id())
      .withName(team.name())
      .withFirstPlayer(PlayerDocument.fromPlayer(team.firstPlayer()))
      .withSecondPlayer(PlayerDocument.fromPlayer(team.secondPlayer()))
      .build();
  }

}
